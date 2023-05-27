package br.com.ctottene.catalog.application.genre.retreive.get;

import br.com.ctottene.catalog.application.UseCaseTest;
import br.com.ctottene.catalog.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;
import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.genre.GenreGateway;
import br.com.ctottene.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_thenShouldReturnGenre() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aGenre =
                Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        // when
        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aGenre.clone()));

        final var actualGenre = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

        verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenGetGenre_thenShouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGateThrowsException_thenShouldReturnException() {
        // given
        final var expectedErrorMessage = "Gateway Error";
        final var expectedId = GenreID.from("123");

        when(genreGateway.findById(eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        // when
        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
