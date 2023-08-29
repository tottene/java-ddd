package br.com.ctottene.catalog.application.castmember.create;

import br.com.ctottene.catalog.Fixture;
import br.com.ctottene.catalog.IntegrationTest;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_thenShouldReturnCastMemberId() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualOutput = useCase.execute(command);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var optionalCastMember = castMemberRepository.findById(actualOutput.id());
        if (optionalCastMember.isPresent()) {
            final var actualCastMember = optionalCastMember.get();
            Assertions.assertEquals(expectedName, actualCastMember.getName());
            Assertions.assertEquals(expectedType, actualCastMember.getType());
            Assertions.assertNotNull(actualCastMember.getCreatedAt());
            Assertions.assertNotNull(actualCastMember.getUpdatedAt());
        }
        verify(castMemberGateway).create(any());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateCastMember_shouldReturnDomainException() {
        // given
        final var expectedName = " ";
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var command =
                CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidType_whenCallsCreateCastMember_shouldReturnDomainException() {
        // given
        final var expectedName = Fixture.name();

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var command =
                CreateCastMemberCommand.with(expectedName, null);

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateCastMember_shouldReturnDomainException() {
        // given
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command =
                CreateCastMemberCommand.with(null, expectedType);

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }
}
