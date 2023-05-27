package br.com.ctottene.catalog.application.genre.retrieve.get;

import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.genre.GenreGateway;
import br.com.ctottene.catalog.domain.genre.GenreID;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String anIn) {
        final var anGenreID = GenreID.from(anIn);
        return this.genreGateway.findById(anGenreID)
                .map(GenreOutput::from)
                .orElseThrow(notFound(anGenreID));
    }

    private static Supplier<NotFoundException> notFound(final GenreID anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }
}
