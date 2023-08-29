package br.com.ctottene.catalog.application.castmember.create;

import br.com.ctottene.catalog.application.Fixture;
import br.com.ctottene.catalog.application.UseCaseTest;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class CreateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_thenShouldReturnCastMemberId() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        when(castMemberGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(command);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(castMemberGateway, times(1))
                .create(argThat(castMember ->
                        Objects.nonNull(castMember.getId())
                                && Objects.equals(expectedName, castMember.getName())
                                && Objects.equals(expectedType, castMember.getType())
                                && Objects.nonNull(castMember.getCreatedAt())
                                && Objects.nonNull(castMember.getUpdatedAt())
                ));
    }

    @Test
    public void giveAnInvalidEmptyName_whenCallsCreateCastMember_thenShouldThrowsNotificationException() {
        // given
        final var expectedName = " ";
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount  = 1;

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->  useCase.execute(command));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void giveAnInvalidNullName_whenCallsCreateCastMember_thenShouldThrowsNotificationException() {
        // given
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount  = 1;

        final var command = CreateCastMemberCommand.with(null, expectedType);

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->  useCase.execute(command));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void giveAnInvalidNullType_whenCallsCreateCastMember_thenShouldThrowsNotificationException() {
        // given
        final var expectedName = Fixture.name();
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount  = 1;

        final var command = CreateCastMemberCommand.with(expectedName, null);

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->  useCase.execute(command));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }
}
