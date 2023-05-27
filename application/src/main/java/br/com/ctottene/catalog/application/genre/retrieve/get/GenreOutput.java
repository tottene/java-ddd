package br.com.ctottene.catalog.application.genre.retrieve.get;

import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.genre.GenreID;

import java.time.Instant;
import java.util.List;

public record GenreOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static GenreOutput from(final Genre aGenre) {
        return new GenreOutput(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories().stream()
                        .map(CategoryID::getValue).toList(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
