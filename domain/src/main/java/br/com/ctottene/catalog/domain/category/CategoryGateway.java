package br.com.ctottene.catalog.domain.category;

import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category aCategory);

    void deleteById(CategoryID anId);

    Optional<Category> findById(CategoryID anId);
    Category update(Category aCategory);

    Pagination<Category> findAll(SearchQuery aQuery);
    List<CategoryID> existsByIds(Iterable<CategoryID> ids);
}
