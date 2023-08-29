package br.com.ctottene.catalog.infrastructure.configuration.usecases;

import br.com.ctottene.catalog.application.castmember.create.CreateCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import br.com.ctottene.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import br.com.ctottene.catalog.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import br.com.ctottene.catalog.application.castmember.retrieve.list.ListCastMembersUseCase;
import br.com.ctottene.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import br.com.ctottene.catalog.application.castmember.update.UpdateCastMemberUseCase;
import br.com.ctottene.catalog.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(CastMemberGateway genreGateway) {
        this.castMemberGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }
    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }
    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }
    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }
}
