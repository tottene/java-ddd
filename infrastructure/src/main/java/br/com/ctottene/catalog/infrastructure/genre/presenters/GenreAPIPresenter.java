package br.com.ctottene.catalog.infrastructure.genre.presenters;

import br.com.ctottene.catalog.application.genre.retrieve.get.GenreOutput;
import br.com.ctottene.catalog.application.genre.retrieve.list.GenreListOutput;
import br.com.ctottene.catalog.infrastructure.genre.models.GenreListResponse;
import br.com.ctottene.catalog.infrastructure.genre.models.GenreResponse;

public interface GenreAPIPresenter {

    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.categories(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id().getValue(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
