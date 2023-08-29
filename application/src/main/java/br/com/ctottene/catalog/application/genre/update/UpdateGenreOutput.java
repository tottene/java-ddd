package br.com.ctottene.catalog.application.genre.update;

import br.com.ctottene.catalog.domain.genre.Genre;

public record UpdateGenreOutput(
        String id
) {
    public static UpdateGenreOutput from(final String id) {
        return new UpdateGenreOutput(id);
    }

    public static UpdateGenreOutput from(final Genre genre) {
        return new UpdateGenreOutput(genre.getId().getValue());
    }
}
