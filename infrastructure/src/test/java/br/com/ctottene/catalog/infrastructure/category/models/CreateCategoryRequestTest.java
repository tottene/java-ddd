package br.com.ctottene.catalog.infrastructure.category.models;

import br.com.ctottene.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CreateCategoryRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched categorie";
        final var expectedIsActive = false;

        final var response = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched categorie";
        final var expectedIsActive = true;

        final var json = """
             {
                "name": "%s",               
                "description": "%s",               
                "is_active": "%s"              
             } 
             """.formatted(
                    expectedName,
                    expectedDescription,
                    expectedIsActive
            );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
