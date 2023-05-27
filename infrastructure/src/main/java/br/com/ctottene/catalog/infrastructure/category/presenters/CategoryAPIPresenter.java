package br.com.ctottene.catalog.infrastructure.category.presenters;

import br.com.ctottene.catalog.application.category.retrieve.get.CategoryOutput;
import br.com.ctottene.catalog.application.category.retrieve.list.CategoryListOutput;
import br.com.ctottene.catalog.infrastructure.category.models.CategoryResponse;
import br.com.ctottene.catalog.infrastructure.category.models.CategoryListResponse;

public interface CategoryAPIPresenter {

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
