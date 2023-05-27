package br.com.ctottene.catalog.infrastructure.genre.models;

import br.com.ctottene.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
public class UpdateGenreRequestTest {

    @Autowired
    private JacksonTester<UpdateGenreRequest> json;

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategory = "123";

        final var json = """
         {
            "name": "%s",
            "is_active": "%s",
            "categories_id": ["%s"]
         } 
         """.formatted(
                expectedName,
                expectedIsActive,
                expectedCategory
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory));
    }
}
