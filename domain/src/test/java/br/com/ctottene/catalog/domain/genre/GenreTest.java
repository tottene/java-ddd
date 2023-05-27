package br.com.ctottene.catalog.domain.genre;

import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedIsActive = true;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> Genre.newGenre(null, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final var expectedName = "  ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedIsActive = true;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenCallNewGenreValidate_thenShouldReceiveError() {
        final var expectedName = """
            Ao contrário do que se acredita, Lorem Ipsum não é simplesmente um texto randômico.
            Com mais de 2000 anos, suas raízes podem ser encontradas em uma obra de literatura latina clássica datada de 45 AC.
            Richard McClintock, um professor de latim do Hampden-Sydney College na Virginia
            """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must between 1 and 255 characters";
        final var expectedIsActive = true;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAnActiveGenre_whenCallDeactivate_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "Movies";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);
        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);
        actualGenre.deactivate();
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, false);
        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);
        actualGenre.activate();
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveGenre_whenCallUpdateWithActivate_shouldReceiveGenreUpdated() throws InterruptedException {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("act", false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() throws InterruptedException {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("act", true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualGenre = Genre.newGenre("act", false);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> actualGenre.update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualGenre = Genre.newGenre("act", false);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> actualGenre.update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre("act", expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);
        Assertions.assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, null);
        });

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOK() throws InterruptedException {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(2);
        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOK() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK() throws InterruptedException {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);
        actualGenre.removeCategory(seriesID);

        Assertions.assertEquals(1, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOK() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(null);

        Assertions.assertEquals(2, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategories_shouldReceiveOK() throws InterruptedException {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(1);
        actualGenre.addCategories(expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOK() {

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategories(expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithNullList_shouldReceiveOK() {

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategories(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }
}
