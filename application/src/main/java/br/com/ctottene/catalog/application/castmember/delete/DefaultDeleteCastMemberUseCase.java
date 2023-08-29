package br.com.ctottene.catalog.application.castmember.delete;

import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;

import java.util.Objects;

public non-sealed class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultDeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(final String id) {
        this.castMemberGateway.deleteById(CastMemberID.from(id));
    }
}
