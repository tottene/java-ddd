package br.com.ctottene.catalog.domain.genre;

import br.com.ctottene.catalog.domain.AggregateRoot;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import br.com.ctottene.catalog.domain.utils.InstantUtils;
import br.com.ctottene.catalog.domain.validation.ValidationHandler;
import br.com.ctottene.catalog.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Genre extends AggregateRoot<GenreID> implements Cloneable {

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> aCategories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = aCategories;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdatedAt, "'updatedAt' should not be null");
        this.deletedAt = aDeletedAt;

        selfValidate();
    }

    // Factory Method
    public static Genre newGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(anId, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    // Factory Method with
    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> aCategories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new Genre(
                anId,
                aName,
                isActive,
                aCategories,
                aCreatedAt,
                anUpdatedAt,
                aDeletedAt
        );
    }

    // Factory Method clone
    public static Genre with(final Genre aGenre) {
        return with(
                aGenre.getId(),
                aGenre.name,
                aGenre.isActive(),
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre update(final String aName, final boolean isActive, final List<CategoryID> aCategories) {
        this.name = aName;
        if (isActive) activate();
        else deactivate();
        this.categories = new ArrayList<>(aCategories != null ? aCategories : Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        selfValidate();

        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create an Aggregate Genre" , notification);
        }
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null ) {
            return this;
        }
        this.categories.add(aCategoryID);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre addCategories(final List<CategoryID> categories) {
        if (categories == null || categories.isEmpty()) {
            return this;
        }
        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null ) {
            return this;
        }
        this.categories.remove(aCategoryID);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    @Override
    public Genre clone() {
        try {
            return (Genre) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
