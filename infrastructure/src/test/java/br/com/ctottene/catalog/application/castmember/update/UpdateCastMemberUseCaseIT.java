package br.com.ctottene.catalog.application.castmember.update;

import br.com.ctottene.catalog.Fixture;
import br.com.ctottene.catalog.IntegrationTest;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.category.Category;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static br.com.ctottene.catalog.Fixture.*;
import static br.com.ctottene.catalog.Fixture.CastMember.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnCastMemberId() {
        // given
        final var castMember = castMemberGateway.create(CastMember.newCastMember(name(), type()));

        final var expectedId = castMember.getId();
        final var expectedName = name();
        final var expectedType = type();

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualOutput = useCase.execute(command);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        final var optionalCastMember = castMemberRepository.findById(castMember.getId().getValue());
        if (optionalCastMember.isPresent()) {
            final var actualCastMember = optionalCastMember.get();

            Assertions.assertEquals(expectedName, actualCastMember.getName());
            Assertions.assertEquals(expectedType, actualCastMember.getType());
            Assertions.assertEquals(castMember.getCreatedAt(), actualCastMember.getCreatedAt());
            Assertions.assertTrue(castMember.getUpdatedAt().isBefore(actualCastMember.getUpdatedAt()));
        }
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldReturnNotificationException() {
        // given
        final var castMember = castMemberGateway.create(CastMember.newCastMember(name(), type()));

        final var expectedId = castMember.getId();
        final var expectedType = type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                null,
                expectedType
        );

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));

        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldReturnNotificationException() {
        // given
        final var castMember = castMemberGateway.create(CastMember.newCastMember(name(), type()));

        final var expectedId = castMember.getId();
        final var expectedName = name();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                null
        );

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));

        verify(castMemberGateway, times(0)).update(any());
    }
}
