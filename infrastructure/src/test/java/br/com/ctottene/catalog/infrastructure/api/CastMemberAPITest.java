package br.com.ctottene.catalog.infrastructure.api;

import br.com.ctottene.catalog.ControllerTest;
import br.com.ctottene.catalog.application.castmember.create.CreateCastMemberOutput;
import br.com.ctottene.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.retrieve.get.CastMemberOutput;
import br.com.ctottene.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import br.com.ctottene.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import br.com.ctottene.catalog.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import br.com.ctottene.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.update.UpdateCastMemberOutput;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.exceptions.DomainException;
import br.com.ctottene.catalog.domain.exceptions.NotFoundException;
import br.com.ctottene.catalog.domain.exceptions.NotificationException;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.validation.Error;
import br.com.ctottene.catalog.domain.validation.handler.Notification;
import br.com.ctottene.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import br.com.ctottene.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static br.com.ctottene.catalog.Fixture.CastMember.type;
import static br.com.ctottene.catalog.Fixture.name;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @MockBean
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    @MockBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private DefaultListCastMembersUseCase listCastMembersUseCase;


    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_thenShouldReturnCastMemberId() throws Exception {
        // given
        final var expectedName = name();
        final var expectedType = type();
        final var expectedId = "123";

        final var command = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));

        // when
        final var request = post("/cast_members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cast_members/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(createCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedType, cmd.type())
                ));
    }

    @Test
    public void givenAnInvalidValidCommand_whenCallsCreateCastMember_thenShouldNotification() throws Exception {
        //given
        final var expectedType = type();
        final var expectedNullNameMessage = "'name' should not be null";

        final var command = new CreateCastMemberRequest(null, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedNullNameMessage))));

        // when
        final var request = post("/cast_members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedNullNameMessage)));

        verify(createCastMemberUseCase).execute(argThat((cmd ->
                Objects.isNull(cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        )));
    }

    @Test
    public void givenAnInvalidValidCommand_whenCallsCreateCastMember_thenShouldDomainException() throws Exception {

        // given
        final var expectedType = type();
        final var expectedNullNameMessage = "'name' should not be null";

        final var command = new CreateCastMemberRequest(null, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedNullNameMessage)));

        // when
        final var request = post("/cast_members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedNullNameMessage)));

        verify(createCastMemberUseCase).execute(argThat((cmd ->
                Objects.equals(null, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        )));
    }

    @Test
    public void givenAValidId_whenCallsGetByIdCastMember_thenShouldReturnCastMember() throws Exception {
        // given
        final var expectedName = name();
        final var expectedType = type();

        final var castMember =
                CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId().getValue();

        when(getCastMemberByIdUseCase.execute(any()))
                .thenReturn(CastMemberOutput.from(castMember));

        // when
        final var request = get("/cast_members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.type", equalTo(expectedType.name())))
                .andExpect(jsonPath("$.created_at", equalTo(castMember.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(castMember.getUpdatedAt().toString())));

        verify(getCastMemberByIdUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenGetByIdCastMember_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        when(getCastMemberByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        // when
        final var request = get("/cast_members/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getCastMemberByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_thenShouldReturnCastMemberId() throws Exception {
        // given
        final var expectedName = name();
        final var expectedType = type();
        final var castMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId().getValue();

        when(updateCastMemberUseCase.execute(any()))
                .thenReturn(UpdateCastMemberOutput.from(expectedId));

        // when
        final var command =
                new UpdateCastMemberRequest(expectedName, expectedType);

        final var request = put("/cast_members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
                && Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCastMember_shouldReturnNotFoundException() throws Exception {
        // given
        final var expectedId = "not-found";
        final var expectedName = name();
        final var expectedType = type();
        final var expectedErrorMessage = "CastMember with ID not-found was not found";

        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, CastMemberID.from(expectedId)));

        final var aCommand =
                new UpdateCastMemberRequest(expectedName, expectedType);

        // when
        final var request = put("/cast_members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
                && Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_thenShouldDomainException() throws Exception {
        // given
        final var expectedName = name();
        final var expectedType = type();
        final var expectedNullNameMessage = "'name' should not be null";
        final var castMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId().getValue();

        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedNullNameMessage))));

        final var aCommand =
                new UpdateCastMemberRequest(null, expectedType);

        // when
        final var request = put("/cast_members/{id}", expectedId)
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

        verify(updateCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
                        && Objects.isNull(cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_thenShouldReturnNoContent() throws Exception {
        // given
        final var expectedId = "123";

        doNothing()
                .when(deleteCastMemberUseCase).execute(any());

        // when
        final var request = delete("/cast_members/{id}", expectedId);

        final var response = this.mockMvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(deleteCastMemberUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_thenShouldReturnCastMembers() throws Exception {
        // given
        final var castMember = CastMember.newCastMember(name(),type());
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerms = "act";
        final var expectedSort = "type";
        final var expectedDirection = "desc";
        final var expectedItemCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(CastMemberListOutput.from(castMember));

        when(listCastMembersUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var request = get("/cast_members")
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
                .andExpect(jsonPath("$.items[0].id", equalTo(castMember.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(castMember.getName())))
                .andExpect(jsonPath("$.items[0].type", equalTo(castMember.getType().name())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(castMember.getCreatedAt().toString())));

        verify(listCastMembersUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                && Objects.equals(expectedPerPage, query.perPage())
                && Objects.equals(expectedDirection, query.direction())
                && Objects.equals(expectedSort, query.sort())
                && Objects.equals(expectedTerms, query.terms())
        ));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_thenShouldReturnDefaultCastMembers() throws Exception {
        // given
        final var castMember = CastMember.newCastMember(name(),type());
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(CastMemberListOutput.from(castMember));

        when(listCastMembersUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var request = get("/cast_members")
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
                .andExpect(jsonPath("$.items[0].id", equalTo(castMember.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(castMember.getName())))
                .andExpect(jsonPath("$.items[0].type", equalTo(castMember.getType().name())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(castMember.getCreatedAt().toString())));

        verify(listCastMembersUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
