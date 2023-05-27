package br.com.ctottene.catalog.application.category.update;

import br.com.ctottene.catalog.domain.category.Category;
import br.com.ctottene.catalog.domain.category.CategoryID;

public record UpdateCategoryOutput(
        String id
) {
    public static UpdateCategoryOutput from(final String anId) {
        return new UpdateCategoryOutput(anId);
    }

    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }
}
