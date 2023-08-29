package br.com.ctottene.catalog.infrastructure.castmember.models;

import br.com.ctottene.catalog.Fixture;
import br.com.ctottene.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateCastMemberRequestTest {

    @Autowired
    private JacksonTester<UpdateCastMemberRequest> json;

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var json = """
         {
            "name": "%s",
            "type": "%s"
         }
         """.formatted(
                expectedName,
                expectedType
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType);
    }
}
