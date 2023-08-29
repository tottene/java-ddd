package br.com.ctottene.catalog.application.castmember.retrieve.get;

import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(final String id) {
        final var castMember = CastMemberID.from(id);
        return this.castMemberGateway.findById(castMember)
                .map(CastMemberOutput::from)
                .orElseThrow(notFound(castMember));
    }

    private static Supplier<NotFoundException> notFound(final CastMemberID id) {
        return () -> NotFoundException.with(CastMember.class, id);
    }
}
