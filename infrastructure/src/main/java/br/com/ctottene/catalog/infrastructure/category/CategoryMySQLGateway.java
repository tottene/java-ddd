package br.com.ctottene.catalog.infrastructure.category;

import br.com.ctottene.catalog.domain.category.Category;
import br.com.ctottene.catalog.domain.category.CategoryGateway;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.infrastructure.category.persistance.CategoryJpaEntity;
import br.com.ctottene.catalog.infrastructure.category.persistance.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static br.com.ctottene.catalog.infrastructure.utils.SpecificationUtils.like;

@Component
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Category create(Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anId) {
        final String anIdValue = anId.getValue();
        if (this.repository.existsById(anIdValue)) {
            this.repository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return this.repository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery aQuery) {
        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CategoryID> existsByIds(final Iterable<CategoryID> categoryIDs) {
        final var ids = StreamSupport.stream(categoryIDs.spliterator(), false)
                .map(CategoryID::getValue)
                .toList();
        return this.repository.existsByIds(ids).stream()
                .map(CategoryID::from)
                .toList();
    }

    private Category save(final Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    private Specification<CategoryJpaEntity> assembleSpecification(final String str) {
        final Specification<CategoryJpaEntity> nameLike = like("name", str);
        final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
        return nameLike.or(descriptionLike);
    }
}
