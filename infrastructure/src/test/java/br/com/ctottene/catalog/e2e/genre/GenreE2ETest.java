package br.com.ctottene.catalog.e2e.genre;

import br.com.ctottene.catalog.E2ETest;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.e2e.MockDsl;
import br.com.ctottene.catalog.infrastructure.genre.models.UpdateGenreRequest;
import br.com.ctottene.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER
            = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return this.mockMvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = retrieveAGenre(actualId.getValue());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                && expectedCategories.containsAll(actualGenre.categories())
        );
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
        Assertions.assertEquals(1, genreRepository.count());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var optionalGenre = genreRepository.findById(actualId.getValue());
        if (optionalGenre.isPresent()) {
            final var actualGenre = optionalGenre.get();

            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertTrue(
                    expectedCategories.size() == actualGenre.getCategoryIDs().size()
                            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
            );
            Assertions.assertNotNull(actualGenre.getCreatedAt());
            Assertions.assertNotNull(actualGenre.getUpdatedAt());
            Assertions.assertNull(actualGenre.getDeletedAt());
        }
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateThruAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Terror", true, List.of());
        givenAGenre("Comedy", true, List.of());

        listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Action")));

        listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Comedy")));

        listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Terror")));

        listGenres(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action",true, List.of());
        givenAGenre("Terror",true, List.of());
        givenAGenre("Comedy",true, List.of());

        listGenres(0, 1, "act")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Action")));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Terror", true, List.of());
        givenAGenre("Comedy", true, List.of());

        listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Terror")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Comedy")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Action")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = retrieveAGenre(actualId.getValue());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                        && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedId = "123";
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        final var request = get("/genres/" + expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre("Action",true, expectedCategories);

        final var requestBody = new UpdateGenreRequest(expectedName, expectedIsActive, mapTo(expectedCategories, CategoryID::getValue));

        updateAGenre(actualId.getValue(), requestBody)
                .andExpect(status().isOk());

        final var optionalGenre = genreRepository.findById(actualId.getValue());
        if (optionalGenre.isPresent()) {
            final var actualGenre = optionalGenre.get();

            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertTrue(
                    expectedCategories.size() == actualGenre.getCategoryIDs().size()
                            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
            );
            Assertions.assertNotNull(actualGenre.getCreatedAt());
            Assertions.assertNotNull(actualGenre.getUpdatedAt());
            Assertions.assertNull(actualGenre.getDeletedAt());
        }
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, true, expectedCategories);

        final var requestBody = new UpdateGenreRequest(expectedName, expectedIsActive, mapTo(expectedCategories, CategoryID::getValue));

        updateAGenre(actualId.getValue(), requestBody)
                .andExpect(status().isOk());

        final var optionalGenre = genreRepository.findById(actualId.getValue());
        if (optionalGenre.isPresent()) {
            final var actualGenre = optionalGenre.get();

            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertNotNull(actualGenre.getCreatedAt());
            Assertions.assertNotNull(actualGenre.getUpdatedAt());
            Assertions.assertNotNull(actualGenre.getDeletedAt());
        }
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, false, expectedCategories);

        final var requestBody = new UpdateGenreRequest(expectedName, expectedIsActive, mapTo(expectedCategories, CategoryID::getValue));

        updateAGenre(actualId.getValue(), requestBody)
                .andExpect(status().isOk());

        final var optionalGenre = genreRepository.findById(actualId.getValue());
        if (optionalGenre.isPresent()) {
            final var actualGenre = optionalGenre.get();

            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertNotNull(actualGenre.getCreatedAt());
            Assertions.assertNotNull(actualGenre.getUpdatedAt());
            Assertions.assertNull(actualGenre.getDeletedAt());
        }
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var actualId = givenAGenre("Movies", true, List.of());

        deleteAGenre(actualId.getValue())
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.genreRepository.existsById(actualId.getValue()));
    }

    @Test
    public void asACatalogAdminIShouldNotSeeAnErrorByDeletingANotExistentGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        deleteAGenre("invalid_id")
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, genreRepository.count());
    }
}
