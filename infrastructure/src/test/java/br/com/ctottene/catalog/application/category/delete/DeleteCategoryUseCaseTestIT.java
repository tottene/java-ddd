package br.com.ctottene.catalog.application.category.delete;

import br.com.ctottene.catalog.IntegrationTest;
import br.com.ctottene.catalog.domain.category.Category;
import br.com.ctottene.catalog.domain.category.CategoryGateway;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.infrastructure.category.persistance.CategoryJpaEntity;
import br.com.ctottene.catalog.infrastructure.category.persistance.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseTestIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenDeleteCategory_thenShouldBeOk() {
        final var aCategory = Category.newCategory("Movies", "Most watched category", true);
        final var expectedId = aCategory.getId();

        Assertions.assertEquals(0, categoryRepository.count());

        save(aCategory);
        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenDeleteCategory_thenShouldBeNotOk() {
        final var expectedId = CategoryID.from("123");

        Assertions.assertEquals(0, categoryRepository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAValidId_whenGateThrowsError_thenShouldReturnException() {
        final var aCategory = Category.newCategory("Movies", "Most watched category", true);
        final var expectedId = aCategory.getId();

        Assertions.assertEquals(0, categoryRepository.count());

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, categoryRepository.count());
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }
}
