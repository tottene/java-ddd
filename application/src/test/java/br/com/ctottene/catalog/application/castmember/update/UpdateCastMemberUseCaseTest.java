package br.com.ctottene.catalog.application.castmember.update;

import br.com.ctottene.catalog.application.Fixture;
import br.com.ctottene.catalog.application.UseCaseTest;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnCastMemberId() {
        // given
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = castMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(castMember)));

        when(castMemberGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(command);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));

        verify(castMemberGateway, times(1)).update(argThat(updatedCastMember ->
                Objects.equals(expectedId, updatedCastMember.getId())
                        && Objects.equals(expectedName, updatedCastMember.getName())
                        && Objects.equals(expectedType, updatedCastMember.getType())
                        && Objects.equals(castMember.getCreatedAt(), updatedCastMember.getCreatedAt())
                        && castMember.getUpdatedAt().isBefore(updatedCastMember.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = castMember.getId();
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                null,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(castMember)));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));

        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = castMember.getId();
        final var expectedName = Fixture.name();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                null
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(castMember)));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));

        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        // given
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway, times(1)).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }
}
