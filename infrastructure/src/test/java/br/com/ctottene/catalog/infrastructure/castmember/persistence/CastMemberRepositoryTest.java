package br.com.ctottene.catalog.infrastructure.castmember.persistence;

import br.com.ctottene.catalog.Fixture;
import br.com.ctottene.catalog.MySQLGatewayTest;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CastMemberRepositoryTest {

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void giveAnInvalidName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessageError = "not-null property references a null or transient value : br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity.name";

        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMember.type());

        final var entity = CastMemberJpaEntity.from(castMember);
        entity.setName(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> castMemberRepository.save(entity));

        final var actualCause =
            Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }

    @Test
    public void giveAnInvalidCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessageError = "not-null property references a null or transient value : br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity.createdAt";

        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMember.type());

        final var entity = CastMemberJpaEntity.from(castMember);
        entity.setCreatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> castMemberRepository.save(entity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }

    @Test
    public void giveAnInvalidUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessageError = "not-null property references a null or transient value : br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity.updatedAt";

        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMember.type());

        final var entity = CastMemberJpaEntity.from(castMember);
        entity.setUpdatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> castMemberRepository.save(entity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }
}
