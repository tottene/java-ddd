package br.com.ctottene.catalog.application.castmember.retrieve.get;

import br.com.ctottene.catalog.IntegrationTest;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static br.com.ctottene.catalog.Fixture.CastMember.type;
import static br.com.ctottene.catalog.Fixture.name;

@IntegrationTest
public class GetCastMemberByIdUseCaseIT {

    @Autowired
    private GetCastMemberByIdUseCase useCase;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnCastMember() {
        // given
        final var expectedName = name();
        final var expectedType = type();

        final var castMember = castMemberGateway.create(
                CastMember.newCastMember(expectedName, expectedType)
        );

        final var expectedId = castMember.getId();

        // when
        final var actualCastMember = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), actualCastMember.id().getValue());
        Assertions.assertEquals(expectedName, actualCastMember.name());
        Assertions.assertEquals(expectedType, actualCastMember.type());
        Assertions.assertEquals(castMember.getCreatedAt(), actualCastMember.createdAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), actualCastMember.updatedAt());
    }

    @Test
    public void givenAValidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var expectedId = CastMemberID.from("123");

        // when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
