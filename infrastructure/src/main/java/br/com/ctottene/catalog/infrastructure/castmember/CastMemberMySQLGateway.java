package br.com.ctottene.catalog.infrastructure.castmember;

import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberRepository;
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
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository repository;

    public CastMemberMySQLGateway(final CastMemberRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public CastMember create(CastMember castMember) {
        return save(castMember);
    }

    @Override
    public void deleteById(CastMemberID id) {
        final String idValue = id.getValue();
        if (this.repository.existsById(idValue)) {
            this.repository.deleteById(idValue);
        }
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID id) {
        return this.repository.findById(id.getValue())
                .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember update(final CastMember castMember) {
        return save(castMember);
    }

    @Override
    public List<CastMemberID> existsByIds(final Iterable<CastMemberID> castMemberIDs) {
        /*final var ids = StreamSupport.stream(castMemberIDs.spliterator(), false)
                .map(CastMemberID::getValue)
                .toList();
        return this.repository.existsByIds(ids).stream()
                .map(CastMemberID::from)
                .toList();*/
        throw new UnsupportedOperationException();
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Direction.fromString(query.direction()), query.sort())
        );
        final var where = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.repository.findAll(where, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    private CastMember save(final CastMember castMember) {
        return this.repository.save(CastMemberJpaEntity.from(castMember)).toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String str) {
        return like("name", str);
    }
}
