package br.com.ctottene.catalog.domain.castmember;

import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest {

    @Test
    public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        final var expectedName = "Ryan Reynolds";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newCastMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewCastMemberAndValidate_shouldReceiveAnError() {
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> CastMember.newCastMember(null, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewCastMemberAndValidate_shouldReceiveAnError() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> CastMember.newCastMember(expectedName, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenCallNewCastMemberValidate_thenShouldReceiveError() {
        final var expectedName = """
            Ao contrário do que se acredita, Lorem Ipsum não é simplesmente um texto randômico.
            Com mais de 2000 anos, suas raízes podem ser encontradas em uma obra de literatura latina clássica datada de 45 AC.
            Richard McClintock, um professor de latim do Hampden-Sydney College na Virginia
            """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> CastMember.newCastMember(expectedName, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNullType_whenCallNewCastMemberAndValidate_shouldReceiveAnError() {
        final var expectedName = "Jim Carrey";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> CastMember.newCastMember(expectedName, null));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
        final var expectedName = "Jim Carrey";
        final var expectedType = CastMemberType.ACTOR;

        final var actualCastMember = CastMember.newCastMember("jimc", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualCastMember);

        final var actualCreatedAt = actualCastMember.getCreatedAt();
        final var actualUpdatedAt = actualCastMember.getUpdatedAt();

        actualCastMember.update(expectedName, expectedType);

        Assertions.assertNotNull(actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertEquals(actualCreatedAt, actualCastMember.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualCastMember.getUpdatedAt()));
    }

    @Test
    public void givenAnInvalidCastMember_whenCallUpdateWithNullName_shouldReceiveNotificationException() {
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualCastMember = CastMember.newCastMember("jimc", CastMemberType.DIRECTOR);

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> actualCastMember.update(null, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCastMember_whenCallUpdateWithEmptyName_shouldNotificationException() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCastMember = CastMember.newCastMember("jimc", CastMemberType.DIRECTOR);

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> actualCastMember.update(expectedName, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCastMember_whenCallUpdateWithNullType_shouldNotificationException() {
        final var expectedName = "Jim Carrey";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualCastMember = CastMember.newCastMember("jimc", CastMemberType.DIRECTOR);

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> actualCastMember.update(expectedName, null));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCastMember_whenCallUpdateWithNameGreaterThen255_shouldNotificationException() {
        final var expectedName = """
            Ao contrário do que se acredita, Lorem Ipsum não é simplesmente um texto randômico.
            Com mais de 2000 anos, suas raízes podem ser encontradas em uma obra de literatura latina clássica datada de 45 AC.
            Richard McClintock, um professor de latim do Hampden-Sydney College na Virginia
            """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualCastMember = CastMember.newCastMember("jimc", CastMemberType.DIRECTOR);

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> actualCastMember.update(expectedName, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
