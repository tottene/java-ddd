package br.com.ctottene.catalog.infrastructure.castmember;

import br.com.ctottene.catalog.Fixture;
import br.com.ctottene.catalog.MySQLGatewayTest;
import br.com.ctottene.catalog.domain.castmember.CastMember;
import br.com.ctottene.catalog.domain.castmember.CastMemberID;
import br.com.ctottene.catalog.domain.castmember.CastMemberType;
import br.com.ctottene.catalog.domain.pagination.SearchQuery;
import br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import br.com.ctottene.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberGateway;

    @Autowired
    private CastMemberRepository repository;

    @Test
    public void testDependenciesInjected() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(repository);
    }

    @Test
    public void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var castMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId();
        Assertions.assertEquals(0, repository.count());

        // when
        final var actualCastMember = castMemberGateway.create(CastMember.with(castMember));

        // then
        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(expectedId, actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertEquals(castMember.getCreatedAt(), actualCastMember.getCreatedAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), actualCastMember.getUpdatedAt());

        final var optionalCastMember = repository.findById(expectedId.getValue());
        if (optionalCastMember.isPresent()) {
            final var persistedCastMember = optionalCastMember.get();
            Assertions.assertEquals(castMember.getId().getValue(), persistedCastMember.getId());
            Assertions.assertEquals(expectedName, persistedCastMember.getName());
            Assertions.assertEquals(expectedType, persistedCastMember.getType());
            Assertions.assertEquals(castMember.getCreatedAt(), persistedCastMember.getCreatedAt());
            Assertions.assertEquals(castMember.getUpdatedAt(), persistedCastMember.getUpdatedAt());
        }
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateCastMember_shouldUpdateIt() {

        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var castMember = CastMember.newCastMember("Jenifer", CastMemberType.ACTOR);
        final var expectedId = castMember.getId();

        Assertions.assertEquals(0, repository.count());

        repository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals("Jenifer", castMember.getName());
        Assertions.assertEquals(CastMemberType.ACTOR, castMember.getType());

        // when
        final var actualCastMember = castMemberGateway.update(
                CastMember.with(castMember)
                        .update(expectedName, expectedType)
        );

        // then
        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(expectedId, actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertEquals(castMember.getCreatedAt(), actualCastMember.getCreatedAt());
        Assertions.assertTrue(castMember.getUpdatedAt().isBefore(actualCastMember.getUpdatedAt()));

        final var optionalCastMember = repository.findById(expectedId.getValue());
        if (optionalCastMember.isPresent()) {
            final var persistedCastMember = optionalCastMember.get();
            Assertions.assertEquals(expectedName, persistedCastMember.getName());
            Assertions.assertEquals(expectedType, persistedCastMember.getType());
            Assertions.assertEquals(castMember.getCreatedAt(), persistedCastMember.getCreatedAt());
            Assertions.assertTrue(castMember.getUpdatedAt().isBefore(persistedCastMember.getUpdatedAt()));
        }
    }

    @Test
    public void givenAValidCastMember_whenCallsDeleteById_shouldReturnDeleteIt() {
        // given
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMember.type());
        repository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        Assertions.assertEquals(1, repository.count());

        // when
        castMemberGateway.deleteById(CastMemberID.from(castMember.getId().getValue()));

        // then
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAnInvalidCastMember_whenCallsDeleteById_shouldReturnOK() {
        // given
        Assertions.assertEquals(0, repository.count());

        // when
        castMemberGateway.deleteById(CastMemberID.from("invalid"));

        // then
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAPrePersistedCastMember_whenCallsFindById_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var castMember = CastMember.newCastMember(expectedName, expectedType);

        final var expectedId = castMember.getId();

        repository.saveAndFlush(CastMemberJpaEntity.from(castMember));


        // when
        Assertions.assertEquals(1, repository.count());
        final var actualCastMember = castMemberGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(expectedId, actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertEquals(castMember.getCreatedAt(), actualCastMember.getCreatedAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), actualCastMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidCastMemberId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = CastMemberID.from("invalid");

        Assertions.assertEquals(0, repository.count());

        // when
        final var castMember = castMemberGateway.findById(expectedId);

        // then
        if (castMember.isPresent()) {
            Assertions.assertTrue(castMember.isEmpty());
        }
    }

    @Test
    public void givenEmptyCastMembers_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = castMemberGateway.findAll(query);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "Jam,0,10,1,1,James Cameron",
            "Jen,0,10,1,1,Jenifer Aniston",
            "Rey,0,10,1,1,Ryan Reynolds",
            "Spi,0,10,1,1,Steven Spielberg",
            "Sc,0,10,1,1,Scarlett Johansson",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCastMember
    ) {
        // given
        mockCastMembers();
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = castMemberGateway.findAll(query);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedCastMember, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,James Cameron",
            "name,desc,0,10,5,5,Steven Spielberg",
            "createdAt,asc,0,10,5,5,Scarlett Johansson",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCastMemberName
    ) {
        // given
        mockCastMembers();
        final var expectedTerms = "";

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = castMemberGateway.findAll(query);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedCastMemberName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,James Cameron;Jenifer Aniston",
            "1,2,2,5,Ryan Reynolds;Scarlett Johansson",
            "2,2,1,5,Steven Spielberg",
    })
    public void givenAValidPaging_whenCallsFindAll_shouldReturnPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCastMember
    ) {
        // given
        mockCastMembers();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = castMemberGateway.findAll(query);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedCastMember.split(";")) {
            final var actualName = actualPage.items().get(index).getName();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockCastMembers() {
        repository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(CastMember.newCastMember("Scarlett Johansson", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Jenifer Aniston", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("James Cameron", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Steven Spielberg", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Ryan Reynolds", CastMemberType.ACTOR))
        ));
    }
}