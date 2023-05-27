package br.com.ctottene.catalog.application.category.retrieve.list;

import br.com.ctottene.catalog.application.UseCase;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
