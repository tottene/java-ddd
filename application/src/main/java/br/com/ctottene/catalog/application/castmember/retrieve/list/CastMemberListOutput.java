package br.com.ctottene.catalog.application.castmember.retrieve.list;

import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
        CastMemberID id,
        String name,
        CastMemberType type,
        Instant createdAt
) {
    public static CastMemberListOutput from(final CastMember castMember) {
        return new CastMemberListOutput(
                castMember.getId(),
                castMember.getName(),
                castMember.getType(),
                castMember.getCreatedAt()
        );
    }
}
