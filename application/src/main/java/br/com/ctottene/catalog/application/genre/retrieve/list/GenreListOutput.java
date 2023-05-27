package br.com.ctottene.catalog.application.genre.retrieve.list;

import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.genre.GenreID;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
        GenreID id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {
    public static GenreListOutput from(final Genre aGenre) {
        return new GenreListOutput(
                aGenre.getId(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
