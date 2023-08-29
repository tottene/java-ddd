package br.com.ctottene.catalog.infrastructure.api.controllers;

import br.com.ctottene.catalog.application.category.create.CreateCategoryCommand;
import br.com.ctottene.catalog.application.category.create.CreateCategoryOutput;
import br.com.ctottene.catalog.application.category.create.CreateCategoryUseCase;
import br.com.ctottene.catalog.application.category.delete.DeleteCategoryUseCase;
import br.com.ctottene.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.ctottene.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import br.com.ctottene.catalog.application.category.update.UpdateCategoryCommand;
import br.com.ctottene.catalog.application.category.update.UpdateCategoryOutput;
import br.com.ctottene.catalog.application.category.update.UpdateCategoryUseCase;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.validation.handler.Notification;
import br.com.ctottene.catalog.infrastructure.api.CategoryAPI;
import br.com.ctottene.catalog.infrastructure.category.models.CategoryListResponse;
import br.com.ctottene.catalog.infrastructure.category.models.CategoryResponse;
import br.com.ctottene.catalog.infrastructure.category.models.CreateCategoryRequest;
import br.com.ctottene.catalog.infrastructure.category.models.UpdateCategoryRequest;
import br.com.ctottene.catalog.infrastructure.category.presenters.CategoryPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private CreateCategoryUseCase createCategoryUseCase;
    private GetCategoryByIdUseCase getCategoryByIdUseCase;
    private UpdateCategoryUseCase updateCategoryUseCase;
    private DeleteCategoryUseCase deleteCategoryUseCase;
    private ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(String search, int page, int perPage, String sort, String dir) {
        return this.listCategoriesUseCase.execute(new SearchQuery(page, perPage, search, sort, dir))
                .map(CategoryPresenter::present);
    }

    @Override
    public CategoryResponse getById(String id) {
        return CategoryPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, UpdateCategoryRequest input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }
}
