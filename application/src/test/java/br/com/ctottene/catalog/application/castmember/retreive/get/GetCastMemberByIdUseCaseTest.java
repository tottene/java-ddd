package br.com.ctottene.catalog.application.castmember.retreive.get;

import br.com.ctottene.catalog.application.Fixture;
import br.com.ctottene.catalog.application.UseCaseTest;
import br.com.ctottene.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberByIdUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCastMember_thenShouldReturnCastMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var castMember =
                CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(castMember));

        // when
        final var actualCastMember = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertNotNull(actualCastMember);
        Assertions.assertEquals(expectedId, actualCastMember.id());
        Assertions.assertEquals(expectedName, actualCastMember.name());
        Assertions.assertEquals(expectedType, actualCastMember.type());
        Assertions.assertEquals(castMember.getCreatedAt(), actualCastMember.createdAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), actualCastMember.updatedAt());

        verify(castMemberGateway).findById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenGetCastMember_thenShouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        when(castMemberGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenGateThrowsException_thenShouldReturnException() {
        // given
        final var expectedErrorMessage = "Gateway Error";
        final var expectedId = CastMemberID.from("123");

        when(castMemberGateway.findById(eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        // when
        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
    }
}
