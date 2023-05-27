package br.com.ctottene.catalog.application.genre.retreive.list;

import br.com.ctottene.catalog.application.UseCaseTest;
import br.com.ctottene.catalog.application.genre.retrieve.list.DefaultListGenresUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.list.GenreListOutput;
import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.genre.GenreGateway;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ListGenresUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListGenresUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListGenres_thenShouldReturnGenres() {
        // given
        final var genres = List.of(
                Genre.newGenre("Action",  true),
                Genre.newGenre("Comedy", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "o";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream().map(GenreListOutput::from).toList();

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        when(genreGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldEmptyGenresList() {
        // given
        final var genres = List.<Genre>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "c";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<GenreListOutput>of();

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        when(genreGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenGateThrowsException_thenShouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "c";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway Error";

        when(genreGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }
}
