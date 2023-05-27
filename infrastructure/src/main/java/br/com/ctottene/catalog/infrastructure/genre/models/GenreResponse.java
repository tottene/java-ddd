package br.com.ctottene.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record GenreResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("is_active") boolean active,
        @JsonProperty("categories_id") List<String> categories,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}
