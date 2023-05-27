package br.com.ctottene.catalog.application.genre.retrieve.list;

import br.com.ctottene.catalog.domain.genre.GenreGateway;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenresUseCase extends ListGenresUseCase {

    private final GenreGateway genreGateway;

    public DefaultListGenresUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery)
                .map(GenreListOutput::from);
    }
}
