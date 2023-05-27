package br.com.ctottene.catalog.application.genre.delete;

import br.com.ctottene.catalog.application.UseCaseTest;
import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.genre.GenreGateway;
import br.com.ctottene.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenDeleteGenre_thenShouldBeOk() {
        // given
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedId = aGenre.getId();

        doNothing()
                .when(genreGateway).deleteById(eq(expectedId));

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        verify(genreGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenDeleteGenre_thenShouldBeNotOk() {
        // given
        final var expectedId = GenreID.from("123");

        doNothing()
                .when(genreGateway).deleteById(eq(expectedId));

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        verify(genreGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenGateThrowsError_thenShouldReturnException() {
        // given
        final var aGenre = Genre.newGenre("Actions", true);
        final var expectedId = aGenre.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(genreGateway).deleteById(eq(expectedId));

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        verify(genreGateway, times(1)).deleteById(eq(expectedId));
    }
}
