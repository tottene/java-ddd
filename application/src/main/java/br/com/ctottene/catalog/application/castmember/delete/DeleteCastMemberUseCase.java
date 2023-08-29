package br.com.ctottene.catalog.application.castmember.delete;

import br.com.ctottene.catalog.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
