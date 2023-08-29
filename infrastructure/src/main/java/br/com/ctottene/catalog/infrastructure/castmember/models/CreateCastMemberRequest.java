package br.com.ctottene.catalog.infrastructure.castmember.models;

import br.com.ctottene.catalog.domain.castmember.CastMemberType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCastMemberRequest(

        @JsonProperty("name") String name,
        @JsonProperty("type") CastMemberType type
) {
}
