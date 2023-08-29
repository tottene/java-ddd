package br.com.ctottene.catalog.infrastructure.api.controllers;

import br.com.ctottene.catalog.application.genre.create.CreateGenreCommand;
import br.com.ctottene.catalog.application.genre.create.CreateGenreUseCase;
import br.com.ctottene.catalog.application.genre.delete.DeleteGenreUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.list.ListGenresUseCase;
import br.com.ctottene.catalog.application.genre.update.UpdateGenreCommand;
import br.com.ctottene.catalog.application.genre.update.UpdateGenreUseCase;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.infrastructure.api.GenreAPI;
import br.com.ctottene.catalog.infrastructure.genre.models.CreateGenreRequest;
import br.com.ctottene.catalog.infrastructure.genre.models.GenreListResponse;
import br.com.ctottene.catalog.infrastructure.genre.models.GenreResponse;
import br.com.ctottene.catalog.infrastructure.genre.models.UpdateGenreRequest;
import br.com.ctottene.catalog.infrastructure.genre.presenters.GenrePresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenresUseCase listGenresUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final ListGenresUseCase listGenresUseCase
    ) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.listGenresUseCase = Objects.requireNonNull(listGenresUseCase);
    }

    @Override
    public ResponseEntity<?> createGenre(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.active(),
                input.categories()
        );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> listGenres(final String search, int page, int perPage, String sort, String dir) {
        return this.listGenresUseCase.execute(new SearchQuery(page, perPage, search, sort, dir))
                .map(GenrePresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenrePresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                input.name(),
                input.active(),
                input.categories()
        );

        return ResponseEntity.ok().body(this.updateGenreUseCase.execute(aCommand));
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteGenreUseCase.execute(anId);
    }
}
