package br.com.ctottene.catalog.infrastructure.configuration.usecases;

import br.com.ctottene.catalog.application.genre.create.CreateGenreUseCase;
import br.com.ctottene.catalog.application.genre.create.DefaultCreateGenreUseCase;
import br.com.ctottene.catalog.application.genre.delete.DefaultDeleteGenreUseCase;
import br.com.ctottene.catalog.application.genre.delete.DeleteGenreUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.list.DefaultListGenresUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.list.ListGenresUseCase;
import br.com.ctottene.catalog.application.genre.update.DefaultUpdateGenreUseCase;
import br.com.ctottene.catalog.application.genre.update.UpdateGenreUseCase;
import br.com.ctottene.catalog.domain.category.CategoryGateway;
import br.com.ctottene.catalog.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(GenreGateway genreGateway, CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }
    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }
    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }
    @Bean
    public ListGenresUseCase listGenresUseCase() {
        return new DefaultListGenresUseCase(genreGateway);
    }
}
