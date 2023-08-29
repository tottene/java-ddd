package br.com.ctottene.catalog.application.castmember.delete;

import br.com.ctottene.catalog.application.Fixture;
import br.com.ctottene.catalog.application.UseCaseTest;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_whenDeleteCastMember_thenShouldBeOk() {
        // given
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = castMember.getId();

        doNothing()
                .when(castMemberGateway).deleteById(eq(expectedId));

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        verify(castMemberGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenDeleteCastMember_thenShouldBeNotOk() {
        // given
        final var expectedId = CastMemberID.from("123");

        doNothing()
                .when(castMemberGateway).deleteById(eq(expectedId));

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        verify(castMemberGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenGateThrowsError_thenShouldReturnException() {
        // given
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = castMember.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(eq(expectedId));

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        verify(castMemberGateway, times(1)).deleteById(eq(expectedId));
    }
}
