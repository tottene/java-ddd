package br.com.ctottene.catalog.application.genre.retrieve.list;

import br.com.ctottene.catalog.application.UseCase;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;

public abstract class ListGenresUseCase
        extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
