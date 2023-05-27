package br.com.ctottene.catalog.application.genre.update;

import br.com.ctottene.catalog.domain.genre.Genre;

public record UpdateGenreOutput(
        String id
) {
    public static UpdateGenreOutput from(final String anId) {
        return new UpdateGenreOutput(anId);
    }

    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}
