package br.com.ctottene.catalog.application.castmember.update;

import br.com.ctottene.catalog.domain.castmember.CastMemberType;

public record UpdateCastMemberCommand(
        String id,
        String name,
        CastMemberType type
) {
    public static UpdateCastMemberCommand with(
            final String id,
            final String name,
            final CastMemberType type
    ) {
        return new UpdateCastMemberCommand(id, name, type);
    }
}
