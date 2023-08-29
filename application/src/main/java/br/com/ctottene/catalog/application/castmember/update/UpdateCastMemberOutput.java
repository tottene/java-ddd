package br.com.ctottene.catalog.application.castmember.update;

import br.com.ctottene.catalog.domain.castmember.CastMember;

public record UpdateCastMemberOutput(
        String id
) {
    public static UpdateCastMemberOutput from(final String id) { return new UpdateCastMemberOutput(id); }

    public static UpdateCastMemberOutput from(final CastMember castMember) {
        return new UpdateCastMemberOutput(castMember.getId().getValue());
    }

}
