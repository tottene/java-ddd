package br.com.ctottene.catalog.infrastructure.genre.persistence;

import br.com.ctottene.catalog.MySQLGatewayTest;
import br.com.ctottene.catalog.domain.genre.Genre;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void giveAnInvalidName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessageError = "not-null property references a null or transient value : br.com.ctottene.catalog.infrastructure.genre.persistence.GenreJpaEntity.name";

        final var aGenre = Genre.newGenre("Action",true);

        final var anEntity = GenreJpaEntity.from(aGenre);
        anEntity.setName(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> genreRepository.save(anEntity));

        final var actualCause =
            Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }

    @Test
    public void giveAnInvalidCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessageError = "not-null property references a null or transient value : br.com.ctottene.catalog.infrastructure.genre.persistence.GenreJpaEntity.createdAt";

        final var aGenre = Genre.newGenre("Movies",true);

        final var anEntity = GenreJpaEntity.from(aGenre);
        anEntity.setCreatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> genreRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }

    @Test
    public void giveAnInvalidUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessageError = "not-null property references a null or transient value : br.com.ctottene.catalog.infrastructure.genre.persistence.GenreJpaEntity.updatedAt";

        final var aGenre = Genre.newGenre("Movies", true);

        final var anEntity = GenreJpaEntity.from(aGenre);
        anEntity.setUpdatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> genreRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessageError, actualCause.getMessage());

    }
}
