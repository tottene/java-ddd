package br.com.ctottene.catalog.application.castmember.delete;

import br.com.ctottene.catalog.Fixture;
import br.com.ctottene.catalog.IntegrationTest;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenAValidCastMemberId_whenCallsDeleteCastMember_shouldDeleteCastMember() {
        // given
        final var aCastMember = castMemberGateway.create(CastMember.newCastMember(Fixture.name(), Fixture.CastMember.type()));
        final var expectedId = aCastMember.getId();

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // when
        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenAnInvalidCastMemberId_whenCallsDeleteCastMember_shouldBeOk() {
        // given
        castMemberGateway.create(CastMember.newCastMember(Fixture.name(), Fixture.CastMember.type()));

        final var expectedId = CastMemberID.from("123");

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // when
        Assertions.assertEquals(1, castMemberRepository.count());
    }
}
