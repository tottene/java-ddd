package br.com.ctottene.catalog.application.genre.update;

import java.util.List;

public record UpdateGenreCommand(
        String id,
        String name,
        boolean isActive,
        List<String> categories
) {
    public static UpdateGenreCommand with(
            final String anId,
            final String aName,
            final Boolean isActive,
            final List<String> aCategories
    ) {
        return new UpdateGenreCommand(anId, aName, isActive != null ? isActive : true, aCategories);
    }
}
