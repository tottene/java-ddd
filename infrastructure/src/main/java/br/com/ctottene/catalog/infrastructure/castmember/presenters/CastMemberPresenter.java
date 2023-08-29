package br.com.ctottene.catalog.infrastructure.castmember.presenters;

import br.com.ctottene.catalog.application.castmember.retrieve.get.CastMemberOutput;
import br.com.ctottene.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import br.com.ctottene.catalog.infrastructure.castmember.models.CastMemberListResponse;
import br.com.ctottene.catalog.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {

    static CastMemberResponse present(final CastMemberOutput output) {
        return new CastMemberResponse(
                output.id().getValue(),
                output.name(),
                output.type(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput output) {
        return new CastMemberListResponse(
                output.id().getValue(),
                output.name(),
                output.type(),
                output.createdAt()
        );
    }
}
