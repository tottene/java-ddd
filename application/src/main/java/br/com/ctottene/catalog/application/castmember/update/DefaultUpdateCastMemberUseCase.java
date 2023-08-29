package br.com.ctottene.catalog.application.castmember.update;

import br.com.ctottene.catalog.domain.Identifier;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import br.com.ctottene.catalog.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand command) {
        final var id = CastMemberID.from(command.id());
        final var name = command.name();
        final var type = command.type();

        final var castMember = this.castMemberGateway.findById(id)
                .orElseThrow(notFound(id));

        final var notification = Notification.create();
        notification.validate(() -> castMember.update(name, type));

        if (notification.hasError()) {
            notify(notification, command.id());
        }

        return UpdateCastMemberOutput.from(this.castMemberGateway.update(castMember));
    }

    private static Supplier<NotFoundException> notFound(final Identifier id) {
        return () -> NotFoundException.with(CastMember.class, id);
    }

    private static void notify(Notification notification, String id) {
        throw new NotificationException("Could not update Aggregate CastMember %s".formatted(id), notification);
    }
}
