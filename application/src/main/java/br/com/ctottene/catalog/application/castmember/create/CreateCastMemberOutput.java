package br.com.ctottene.catalog.application.castmember.create;

import br.com.ctottene.catalog.domain.castmember.CastMember;

public record CreateCastMemberOutput(
        String id
) {
    public static CreateCastMemberOutput from(final String id) { return new CreateCastMemberOutput(id); }

    public static CreateCastMemberOutput from(final CastMember castMember) {
        return new CreateCastMemberOutput(castMember.getId().getValue());
    }

}
