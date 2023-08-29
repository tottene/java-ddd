package br.com.ctottene.catalog.domain.castmember;

import br.com.ctottene.catalog.domain.AggregateRoot;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import br.com.ctottene.catalog.domain.utils.InstantUtils;
import br.com.ctottene.catalog.domain.validation.ValidationHandler;
import br.com.ctottene.catalog.domain.validation.handler.Notification;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private final Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
            final CastMemberID id,
            final String name,
            final CastMemberType type,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        super(id);
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    public static CastMember newCastMember(final String name, final CastMemberType type) {
        final var id = CastMemberID.unique();
        final var now = InstantUtils.now();
        return new CastMember(id, name, type, now, now);
    }

    public static CastMember with(
            final CastMemberID id,
            final String name,
            final CastMemberType type,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        return new CastMember(id, name, type, createdAt, updatedAt);
    }

    public static CastMember with(final CastMember castMember) {
        return with(
                castMember.id,
                castMember.name,
                castMember.type,
                castMember.createdAt,
                castMember.updatedAt
        );
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public CastMember update(final String name, final CastMemberType type) {
        this.name = name;
        this.type = type;
        this.updatedAt = InstantUtils.now().plus(1, ChronoUnit.NANOS);
        selfValidate();

        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create an Aggregate CastMember" , notification);
        }
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();
    }

}
