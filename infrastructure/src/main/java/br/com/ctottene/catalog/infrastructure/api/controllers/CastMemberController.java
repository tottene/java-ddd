package br.com.ctottene.catalog.infrastructure.api.controllers;

import br.com.ctottene.catalog.application.castmember.create.CreateCastMemberCommand;
import br.com.ctottene.catalog.application.castmember.create.CreateCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import br.com.ctottene.catalog.application.castmember.retrieve.list.ListCastMembersUseCase;
import br.com.ctottene.catalog.application.castmember.update.UpdateCastMemberCommand;
import br.com.ctottene.catalog.application.castmember.update.UpdateCastMemberUseCase;
import br.com.ctottene.catalog.domain.pagination.Pagination;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.infrastructure.api.CastMemberAPI;
import br.com.ctottene.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import br.com.ctottene.catalog.infrastructure.castmember.models.CastMemberListResponse;
import br.com.ctottene.catalog.infrastructure.castmember.models.CastMemberResponse;
import br.com.ctottene.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import br.com.ctottene.catalog.infrastructure.castmember.presenters.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase,
            final ListCastMembersUseCase listCastMembersUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
    }

    @Override
    public ResponseEntity<?> createCastMember(final CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(
                input.name(),
                input.type()
        );

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> listCastMembers(final String search, int page, int perPage, String sort, String dir) {
        return this.listCastMembersUseCase.execute(new SearchQuery(page, perPage, search, sort, dir))
                .map(CastMemberPresenter::present);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, UpdateCastMemberRequest input) {
        final var aCommand = UpdateCastMemberCommand.with(
                id,
                input.name(),
                input.type()
        );

        return ResponseEntity.ok().body(this.updateCastMemberUseCase.execute(aCommand));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}
