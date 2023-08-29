package br.com.ctottene.catalog.e2e;

import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.castmember.CastMemberType;
import br.com.ctottene.catalog.domain.category.CategoryID;
import br.com.ctottene.catalog.domain.genre.GenreID;
import br.com.ctottene.catalog.infrastructure.castmember.models.CastMemberResponse;
import br.com.ctottene.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import br.com.ctottene.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import br.com.ctottene.catalog.infrastructure.category.models.CategoryResponse;
import br.com.ctottene.catalog.infrastructure.category.models.CreateCategoryRequest;
import br.com.ctottene.catalog.infrastructure.category.models.UpdateCategoryRequest;
import br.com.ctottene.catalog.infrastructure.configuration.json.Json;
import br.com.ctottene.catalog.infrastructure.genre.models.CreateGenreRequest;
import br.com.ctottene.catalog.infrastructure.genre.models.GenreResponse;
import br.com.ctottene.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var requestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", requestBody);

        return CategoryID.from(actualId);
    }

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> aCategories) throws Exception {
        final var requestBody = new CreateGenreRequest(aName, isActive, mapTo(aCategories, CategoryID::getValue));
        final var actualId =  this.given("/genres", requestBody);

        return GenreID.from(actualId);
    }

    default CastMemberID givenACastMember(final String name, final CastMemberType type) throws Exception {
        final var requestBody = new CreateCastMemberRequest(name, type);
        final var actualId =  this.given("/cast_members", requestBody);

        return CastMemberID.from(actualId);
    }

    default CategoryResponse retrieveACategory(final String id) throws Exception {
        return retrieve("/categories/" + id, CategoryResponse.class);
    }

    default GenreResponse retrieveAGenre(final String id) throws Exception {
        return retrieve("/genres/" + id, GenreResponse.class);
    }

    default CastMemberResponse retrieveACastMember(final String id) throws Exception {
        return retrieve("/cast_members/" + id, CastMemberResponse.class);
    }

    default ResultActions updateACategory(final String id, final UpdateCategoryRequest request) throws Exception {
        return update("/categories/" + id, request);
    }

    default ResultActions updateAGenre(final String id, final UpdateGenreRequest request) throws Exception {
        return update("/genres/" + id, request);
    }

    default ResultActions updateACastMember(final String id, final UpdateCastMemberRequest request) throws Exception {
        return update("/cast_members/" + id, request);
    }

    default ResultActions deleteACategory(final String id) throws Exception {
        return this.delete("/categories/" + id);
    }

    default ResultActions deleteAGenre(final String id) throws Exception {
        return this.delete("/genres/" + id);
    }

    default ResultActions deleteACastMember(final String id) throws Exception {
        return this.delete("/cast_members/" + id);
    }

    default ResultActions listCategories(final int page, final int perPage, final String terms) throws Exception {
        return listCategories(page, perPage, terms, "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String terms) throws Exception {
        return listGenres(page, perPage, terms, "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String terms) throws Exception {
        return listCastMembers(page, perPage, terms, "", "");
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return listCastMembers(page, perPage, "", "", "");
    }

    default ResultActions listCategories(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return list("/categories", page, perPage, search, sort, direction);
    }

    default ResultActions listGenres(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return list("/genres", page, perPage, search, sort, direction);
    }

    default ResultActions listCastMembers(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return list("/cast_members", page, perPage, search, sort, direction);
    }

    private String given(final String url, final Object body) throws Exception {
        final var request = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var actualId = this.mvc().perform(request)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");

        return actualId;
    }

    private ResultActions list(
            final String url,
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var request = get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(request);
    }

    private <T> T retrieve(String url, Class<T> clazz) throws Exception {
        final var request = get(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc().perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(
            final String url
    ) throws Exception {
        final var request = MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(request);
    }

    private ResultActions update(
            final String url,
            final Object body
    ) throws Exception {
        final var request = MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(request);
    }

    default <R, T> List<T> mapTo(final List<R> actual, final Function<R, T> mapper) {
        return actual.stream()
                .map(mapper)
                .toList();
    }
}
