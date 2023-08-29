package br.com.ctottene.catalog.domain.castmember;

import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {

    CastMember create(CastMember castMember);
    void deleteById(CastMemberID id);
    Optional<CastMember> findById(CastMemberID id);
    CastMember update(CastMember castMember);
    Pagination<CastMember> findAll(SearchQuery query);
    List<CastMemberID> existsByIds(Iterable<CastMemberID> ids);
}
