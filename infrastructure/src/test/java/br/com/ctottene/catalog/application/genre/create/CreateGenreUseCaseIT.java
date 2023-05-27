package br.com.ctottene.catalog.application.genre.create;

import br.com.ctottene.catalog.IntegrationTest;
import br.com.ctottene.catalog.domain.Identifier;
import br.com.ctottene.catalog.domain.category.Category;
import br.com.ctottene.catalog.domain.category.CategoryGateway;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import br.com.ctottene.catalog.domain.genre.GenreGateway;
import br.com.ctottene.catalog.infrastructure.genre.persistance.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

    @Autowired
    private GenreRepository genreRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() {
        // given
        final var movies = categoryGateway.create(Category.newCategory("Movies", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        Assertions.assertEquals(0, genreRepository.count());

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var optionalGenre = genreRepository.findById(actualOutput.id());
        if (optionalGenre.isPresent()) {
            final var actualGenre = optionalGenre.get();
            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertTrue(
                    expectedCategories.size() == actualGenre.getCategoryIDs().size()
                            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
            );
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertNotNull(actualGenre.getCreatedAt());
            Assertions.assertNotNull(actualGenre.getUpdatedAt());
            Assertions.assertNull(actualGenre.getDeletedAt());

        }
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var optionalGenre = genreRepository.findById(actualOutput.id());

        if (optionalGenre.isPresent()) {
            final var actualGenre = optionalGenre.get();

            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertTrue(
                    expectedCategories.size() == actualGenre.getCategoryIDs().size()
                            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
            );
            Assertions.assertNotNull(actualGenre.getCreatedAt());
            Assertions.assertNotNull(actualGenre.getUpdatedAt());
            Assertions.assertNull(actualGenre.getDeletedAt());
        }
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var optionalGenre = genreRepository.findById(actualOutput.id());

        if (optionalGenre.isPresent()) {
            final var actualGenre = optionalGenre.get();

            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertTrue(
                    expectedCategories.size() == actualGenre.getCategoryIDs().size()
                            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
            );
            Assertions.assertNotNull(actualGenre.getCreatedAt());
            Assertions.assertNotNull(actualGenre.getUpdatedAt());
            Assertions.assertNotNull(actualGenre.getDeletedAt());
        }
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(null, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var movies = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final var expectName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series.getId(), documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        final var aCommand =
                CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    protected List<String> asString(final List<? extends Identifier> ids) {
        return ids.stream()
                .map(Identifier::getValue)
                .toList();
    }
}
