package br.com.ctottene.catalog.infrastructure.api;

import br.com.ctottene.catalog.ControllerTest;
import br.com.ctottene.catalog.application.genre.create.CreateGenreOutput;
import br.com.ctottene.catalog.application.genre.create.CreateGenreUseCase;
import br.com.ctottene.catalog.application.genre.delete.DeleteGenreUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.get.GenreOutput;
import br.com.ctottene.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import br.com.ctottene.catalog.application.genre.retrieve.list.GenreListOutput;
import br.com.ctottene.catalog.application.genre.retrieve.list.ListGenresUseCase;
import br.com.ctottene.catalog.application.genre.update.UpdateGenreOutput;
import br.com.ctottene.catalog.application.genre.update.UpdateGenreUseCase;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.exceptions.DomainException;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.genre.GenreID;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.validation.Error;
import br.com.ctottene.catalog.domain.validation.handler.Notification;
import br.com.ctottene.catalog.infrastructure.genre.models.CreateGenreRequest;
import br.com.ctottene.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private ListGenresUseCase listGenresUseCase;


    @Test
    public void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() throws Exception {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of("123", "456");
        final var expectedId = "123";

        final var anInput = new CreateGenreRequest(expectedName, expectedIsActive, expectedCategories);

        when(createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        // when
        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(createGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedIsActive, cmd.isActive())
                && Objects.equals(expectedCategories, cmd.categories())
                ));
    }

    @Test
    public void givenAnInvalidValidCommand_whenCallsCreateGenre_thenShouldNotification() throws Exception {
        //given
        final var expectedIsActive = true;
        final var expectedCategories = List.of("123", "456");
        final var expectedNullNameMessage = "'name' should not be null";

        final var anInput = new CreateGenreRequest(null, expectedIsActive, expectedCategories);

        when(createGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedNullNameMessage))));

        // when
        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedNullNameMessage)));

        verify(createGenreUseCase, times(1)).execute(argThat((cmd ->
                Objects.isNull(cmd.name())
                        && Objects.equals(expectedIsActive, cmd.isActive())
                        && Objects.equals(expectedCategories, cmd.categories())
        )));
    }

    @Test
    public void givenAnInvalidValidCommand_whenCallsCreateGenre_thenShouldDomainException() throws Exception {

        // given
        final var expectedIsActive = true;
        final var expectedCategories = List.of("123", "456");
        final var expectedNullNameMessage = "'name' should not be null";

        final var anInput = new CreateGenreRequest(null, expectedIsActive, expectedCategories);

        when(createGenreUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedNullNameMessage)));

        // when
        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedNullNameMessage)));

        verify(createGenreUseCase, times(1)).execute(argThat((cmd ->
                Objects.equals(null, cmd.name())
                        && Objects.equals(expectedIsActive, cmd.isActive())
                        && Objects.equals(expectedCategories, cmd.categories())
        )));
    }

    @Test
    public void givenAValidId_whenCallsGetByIdGenre_thenShouldReturnGenre() throws Exception {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of("123", "456");

        final var aGenre =
                Genre.newGenre(expectedName, expectedIsActive)
                        .addCategories(
                                expectedCategories.stream()
                                        .map(CategoryID::from)
                                        .toList()
                        );
        final var expectedId = aGenre.getId().getValue();

        when(getGenreByIdUseCase.execute(any()))
                .thenReturn(GenreOutput.from(aGenre));

        // when
        final var request = get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.categories_id", equalTo(expectedCategories)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aGenre.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aGenre.getDeletedAt())));

        verify(getGenreByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenGetByIdGenre_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(getGenreByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        // when
        final var request = get("/genres/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getGenreByIdUseCase, times(1)).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_thenShouldReturnGenreId() throws Exception {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of("123", "456");
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId().getValue();

        when(updateGenreUseCase.execute(any()))
                .thenReturn(UpdateGenreOutput.from(expectedId));

        // when
        final var aCommand =
                new UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories);

        final var request = put("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
                && Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedIsActive, cmd.isActive())
                && Objects.equals(expectedCategories, cmd.categories())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateGenre_shouldReturnNotFoundException() throws Exception {
        // given
        final var expectedId = "not-found";
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = List.of("123", "456");
        final var expectedErrorMessage = "Genre with ID not-found was not found";

        when(updateGenreUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Genre.class, GenreID.from(expectedId)));

        final var aCommand =
                new UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories);

        // when
        final var request = put("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
                && Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_thenShouldDomainException() throws Exception {
        // given
        final var expectedIsActive = true;
        final var expectedCategories = List.of("123", "456");
        final var expectedNullNameMessage = "'name' should not be null";
        final var aGenre = Genre.newGenre("action", expectedIsActive);
        final var expectedId = aGenre.getId().getValue();

        when(updateGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedNullNameMessage))));

        final var aCommand =
                new UpdateGenreRequest(null, expectedIsActive, expectedCategories);

        // when
        final var request = put("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedNullNameMessage)));

        verify(updateGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
                        && Objects.isNull(cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenre_thenShouldReturnNoContent() throws Exception {
        // given
        final var expectedId = "123";

        doNothing()
                .when(deleteGenreUseCase).execute(any());

        // when
        final var request = delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(deleteGenreUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAValidQuery_whenCallsListGenres_thenShouldReturnGenres() throws Exception {
        // given
        final var aGenre = Genre.newGenre("Action",true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "action";
        final var expectedSort = "name";
        final var expectedDirection = "desc";
        final var expectedItemCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(GenreListOutput.from(aGenre));

        when(listGenresUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var request = get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aGenre.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aGenre.getName())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aGenre.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aGenre.getDeletedAt())));

        verify(listGenresUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                && Objects.equals(expectedPerPage, query.perPage())
                && Objects.equals(expectedDirection, query.direction())
                && Objects.equals(expectedSort, query.sort())
                && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
