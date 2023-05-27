package br.com.ctottene.catalog.infrastructure.category;

import br.com.ctottene.catalog.MySQLGatewayTest;
import br.com.ctottene.catalog.domain.category.Category;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.infrastructure.category.persistance.CategoryJpaEntity;
import br.com.ctottene.catalog.infrastructure.category.persistance.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Category most watched";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.create(aCategory);
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(aCategory.getDeletedAt());

        var optionalEntity = categoryRepository.findById(aCategory.getId().getValue());
        if (optionalEntity.isPresent()) {
            final var actualEntity = optionalEntity.get();
            Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
            Assertions.assertEquals(expectedName, actualEntity.getName());
            Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
            Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
            Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
            Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
            Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
            Assertions.assertNull(aCategory.getDeletedAt());
        }
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnACategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Category most watched";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Movie", null, expectedIsActive);
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        var optionalInvalidEntity = categoryRepository.findById(aCategory.getId().getValue());
        if (optionalInvalidEntity.isPresent()) {
            final var actualInvalidEntity = optionalInvalidEntity.get();
            Assertions.assertEquals("Movie", actualInvalidEntity.getName());
            Assertions.assertNull(actualInvalidEntity.getDescription());
            Assertions.assertEquals(expectedIsActive, actualInvalidEntity.isActive());
        }

        final var anUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription, expectedIsActive);
        final var actualCategory = categoryGateway.update(anUpdatedCategory);
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(aCategory.getDeletedAt());

        var optionalEntity = categoryRepository.findById(aCategory.getId().getValue());
        if (optionalEntity.isPresent()) {
            final var actualEntity = optionalEntity.get();
            Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
            Assertions.assertEquals(expectedName, actualEntity.getName());
            Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
            Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
            Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
            Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
            Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
            Assertions.assertNull(aCategory.getDeletedAt());
        }
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        final var aCategory = Category.newCategory("Movies", "Most watched category", true);
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(aCategory.getId());
        Assertions.assertEquals(0, categoryRepository.count());
    }
    @Test
    public void givenAnInValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count());

        categoryGateway.deleteById(CategoryID.from("invalid"));
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Category most watched";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        var optionalCategory = categoryRepository.findById(aCategory.getId().getValue());
        if (optionalCategory.isPresent()) {
            final var actualCategory = optionalCategory.get();
            Assertions.assertEquals(aCategory.getId().getValue(), actualCategory.getId());
            Assertions.assertEquals(expectedName, actualCategory.getName());
            Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
            Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
            Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
            Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
            Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
            Assertions.assertNull(aCategory.getDeletedAt());
        }
    }

    @Test
    public void givenAValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.findById(CategoryID.from("empty"));

        Assertions.assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));
        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var actualResults = categoryGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResults.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResults.perPage());
        Assertions.assertEquals(expectedTotal, actualResults.total());
        Assertions.assertEquals(expectedPerPage, actualResults.items().size());
        Assertions.assertEquals(documentaries.getId(), actualResults.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var actualResults = categoryGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResults.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResults.perPage());
        Assertions.assertEquals(expectedTotal, actualResults.total());
        Assertions.assertEquals(0, actualResults.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllAllPages_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));
        Assertions.assertEquals(3, categoryRepository.count());

        // Page 0
        var query = new SearchQuery(0, 1, "", "name", "asc");
        var actualResults = categoryGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResults.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResults.perPage());
        Assertions.assertEquals(expectedTotal, actualResults.total());
        Assertions.assertEquals(expectedPerPage, actualResults.items().size());
        Assertions.assertEquals(documentaries.getId(), actualResults.items().get(0).getId());

        // Page 1
        expectedPage = 1;
        query = new SearchQuery(1, 1, "", "name", "asc");
        actualResults = categoryGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResults.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResults.perPage());
        Assertions.assertEquals(expectedTotal, actualResults.total());
        Assertions.assertEquals(expectedPerPage, actualResults.items().size());
        Assertions.assertEquals(movies.getId(), actualResults.items().get(0).getId());

        // Page 2
        expectedPage = 2;
        query = new SearchQuery(2, 1, "", "name", "asc");
        actualResults = categoryGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResults.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResults.perPage());
        Assertions.assertEquals(expectedTotal, actualResults.total());
        Assertions.assertEquals(expectedPerPage, actualResults.items().size());
        Assertions.assertEquals(series.getId(), actualResults.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));
        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "doc", "name", "asc");
        final var actualResults = categoryGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResults.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResults.perPage());
        Assertions.assertEquals(expectedTotal, actualResults.total());
        Assertions.assertEquals(expectedPerPage, actualResults.items().size());
        Assertions.assertEquals(documentaries.getId(), actualResults.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndMostWatchedAsTerms_whenCallsFindAllAndTermsMatchesCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", "Most watched category", true);
        final var series = Category.newCategory("Series", "A watched category", true);
        final var documentaries = Category.newCategory("Documentaries", "Less watched category", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));
        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "Most watched category", "name", "asc");
        final var actualResults = categoryGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResults.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResults.perPage());
        Assertions.assertEquals(expectedTotal, actualResults.total());
        Assertions.assertEquals(expectedPerPage, actualResults.items().size());
        Assertions.assertEquals(movies.getId(), actualResults.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsExistsByIds_shouldReturnIds() {
        // given
        final var movies = Category.newCategory("Movies", "Most watched category", true);
        final var series = Category.newCategory("Series", "A watched category", true);
        final var documentaries = Category.newCategory("Documentaries", "Less watched category", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var expectedIds = List.of(movies.getId(), series.getId());

        final var ids = List.of(movies.getId(), series.getId(), CategoryID.from("123"));

        // when
        final var actualResult = categoryGateway.existsByIds(ids);

        Assertions.assertTrue(
                expectedIds.size() == actualResult.size() &&
                        expectedIds.containsAll(actualResult)
        );
    }
}
