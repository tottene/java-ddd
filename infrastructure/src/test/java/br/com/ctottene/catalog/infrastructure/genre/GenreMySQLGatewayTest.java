package br.com.ctottene.catalog.infrastructure.genre;

import br.com.ctottene.catalog.MySQLGatewayTest;
import br.com.ctottene.catalog.domain.category.Category;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.genre.GenreID;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.infrastructure.category.CategoryMySQLGateway;
import br.com.ctottene.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import br.com.ctottene.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjected() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(genreRepository);
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsCreate_shouldPersistGenre() {
        // given
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);
        Assertions.assertEquals(0, genreRepository.count());
        final var expectedId = aGenre.getId();

        // when
        final var actualGenre = genreGateway.create(aGenre);
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(aGenre.getDeletedAt());

        // then
        final var optionalGenre = genreRepository.findById(expectedId.getValue());
        if (optionalGenre.isPresent()) {
            final var persistedGenre = optionalGenre.get();
            Assertions.assertEquals(aGenre.getId().getValue(), persistedGenre.getId());
            Assertions.assertEquals(expectedName, persistedGenre.getName());
            Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
            Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
            Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
            Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
            Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
            Assertions.assertNull(aGenre.getDeletedAt());
        }
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreate_shouldPersistGenre() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, genreRepository.count());
        final var expectedId = aGenre.getId();

        // when
        final var actualGenre = genreGateway.create(aGenre);
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(aGenre.getDeletedAt());

        // then
        final var optionalGenre = genreRepository.findById(expectedId.getValue());
        if (optionalGenre.isPresent()) {
            final var persistedGenre = optionalGenre.get();
            Assertions.assertEquals(aGenre.getId().getValue(), persistedGenre.getId());
            Assertions.assertEquals(expectedName, persistedGenre.getName());
            Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
            Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
            Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
            Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
            Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
            Assertions.assertNull(aGenre.getDeletedAt());
        }
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {

        // given
        final var movies = categoryGateway.create(Category.newCategory("Movies", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId());

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(0, aGenre.getCategories().size());

        // when
        final var actualGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        // then
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var optionalGenre = genreRepository.findById(expectedId.getValue());
        if (optionalGenre.isPresent()) {
            final var persistedGenre = optionalGenre.get();
            Assertions.assertEquals(expectedName, persistedGenre.getName());
            Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
            Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()));
            Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
            Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
            Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
            Assertions.assertNull(aGenre.getDeletedAt());
        }
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {

        // given
        final var movies = categoryGateway.create(Category.newCategory("Movies", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("ac", true);
        aGenre.addCategories(List.of(movies.getId(), series.getId()));
        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(2, aGenre.getCategories().size());

        // when
        final var actualGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var optionalGenre = genreRepository.findById(expectedId.getValue());
        if (optionalGenre.isPresent()) {
            final var persistedGenre = optionalGenre.get();
            Assertions.assertEquals(expectedName, persistedGenre.getName());
            Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
            Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
            Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
            Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
            Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
            Assertions.assertNull(aGenre.getDeletedAt());
        }

    }

    @Test
    public void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        // when
        final var actualGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var optionalGenre = genreRepository.findById(expectedId.getValue());
        if (optionalGenre.isPresent()) {
            final var persistedGenre = optionalGenre.get();
            Assertions.assertEquals(expectedName, persistedGenre.getName());
            Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
            Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
            Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
            Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
            Assertions.assertNull(persistedGenre.getDeletedAt());
        }
    }

    @Test
    public void givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre() {
        // given
        final var expectedName = "Active";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        // when
        final var actualGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var optionalGenre = genreRepository.findById(expectedId.getValue());
        if (optionalGenre.isPresent()) {
            final var persistedGenre = optionalGenre.get();
            Assertions.assertEquals(expectedName, persistedGenre.getName());
            Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
            Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
            Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
            Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
            Assertions.assertNotNull(persistedGenre.getDeletedAt());
        }
    }

    @Test
    public void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK() {
        // given
        final var aGenre = Genre.newGenre("Action", true);
        Assertions.assertEquals(0, genreRepository.count());

        // when
        genreGateway.deleteById(GenreID.from(aGenre.getId().getValue()));

        // then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInValidGenreId_whenTryToDeleteIt_shouldDeleteGenre() {
        // given
        Assertions.assertEquals(0, genreRepository.count());

        // when
        genreGateway.deleteById(GenreID.from("invalid"));

        // then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
        // given
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        // when
        final var actualGenre = genreGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = GenreID.from("123");

        Assertions.assertEquals(0, genreRepository.count());

        // when
        final var actualGenre = genreGateway.findById(expectedId);

        // then
        if (actualGenre.isPresent()) {
            Assertions.assertTrue(actualGenre.isEmpty());
        }
    }

    @Test
    public void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "ac,0,10,1,1,Action",
            "dr,0,10,1,1,Drama",
            "com,0,10,1,1,Comedy",
            "fic,0,10,1,1,Fiction",
            "terr,0,10,1,1,Terror",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        // given
        mockGenres();
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Action",
            "name,desc,0,10,5,5,Terror",
            "createdAt,asc,0,10,5,5,Comedy",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        // given
        mockGenres();
        final var expectedTerms = "";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Action;Comedy",
            "1,2,2,5,Drama;Fiction",
            "2,2,1,5,Terror",
    })
    public void givenAValidPaging_whenCallsFindAll_shouldReturnPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenres
    ) {
        // given
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedGenres.split(";")) {
            final var actualName = actualPage.items().get(index).getName();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockGenres() {
        genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Comedy", true)),
                GenreJpaEntity.from(Genre.newGenre("Action", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Fiction", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true))
        ));
    }

    private List<CategoryID> sorted(final List<CategoryID> categoryIDS) {
        return categoryIDS.stream()
                .sorted(Comparator.comparing(CategoryID::getValue))
                .toList();
    }
}
