package br.com.ctottene.catalog.application.castmember.retrieve.get;

import br.com.ctottene.catalog.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
