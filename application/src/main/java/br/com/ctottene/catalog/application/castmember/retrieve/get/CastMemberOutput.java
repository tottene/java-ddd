package br.com.ctottene.catalog.application.castmember.retrieve.get;

import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberOutput(
        CastMemberID id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {
    public static CastMemberOutput from(final CastMember castMember) {
        return new CastMemberOutput(
                castMember.getId(),
                castMember.getName(),
                castMember.getType(),
                castMember.getCreatedAt(),
                castMember.getUpdatedAt()
        );
    }
}
