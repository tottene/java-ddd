package br.com.ctottene.catalog.application;

import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberType;
import br.com.ctottene.catalog.domain.category.Category;
import br.com.ctottene.catalog.domain.genre.Genre;
import br.com.ctottene.catalog.domain.utils.IdUtils;
import br.com.ctottene.catalog.domain.video.*;
import com.github.javafaker.Faker;

import java.time.Year;
import java.util.Arrays;
import java.util.Set;

import static io.vavr.API.*;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
        );
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Rating.L,
                Set.of(Categories.movies().getId()),
                Set.of(Genres.action().getId()),
                Set.of(CastMembers.henry().getId(), CastMembers.amy().getId())
        );
    }

    public static final class Categories {

        private static final Category MOVIES =
                Category.newCategory("Movies", "Some description", true);

        public static Category movies() {
            return MOVIES.clone();
        }
    }

    public static final class CastMembers {

        private static final CastMember HENRY =
                CastMember.newCastMember("Henry Cavil", CastMemberType.ACTOR);

        private static final CastMember AMY =
                CastMember.newCastMember("Amy Adams", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember henry() {
            return CastMember.with(HENRY);
        }

        public static CastMember amy() {
            return CastMember.with(AMY);
        }
    }

    public static final class Genres {

        private static final Genre ACTION =
                Genre.newGenre("Action", true);

        public static Genre action() {
            return Genre.with(ACTION);
        }
    }

    public static final class Videos {

        private static final Video SUPERMAN = Video.newVideo(
                "Superman",
                description(),
                Year.of(2022),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Rating.L,
                Set.of(Categories.movies().getId()),
                Set.of(Genres.action().getId()),
                Set.of(CastMembers.henry().getId(), CastMembers.amy().getId())
        );

        public static Video superman() {
            return Video.with(SUPERMAN);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static VideoMediaType mediaType() {
            return FAKER.options().option(VideoMediaType.values());
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final String checksum = IdUtils.uuid();
            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }

        public static String description() {
            return FAKER.options().option(
                    """
                            Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                            Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                            Para acessar todas as aulas, lives e desafios, acesse:
                            https://imersao.fullcycle.com.br/
                            """,
                    """
                            Nesse vídeo você entenderá o que é DTO (Data Transfer Object), quando e como utilizar no dia a dia, 
                            bem como sua importância para criar aplicações com alta qualidade.
                            """
            );
        }

       /* public static AudioVideoMedia audioVideo(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return AudioVideoMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/videos/" + checksum
            );
        }*/

        public static ImageMedia image(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return ImageMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/images/" + checksum
            );
        }
    }
}