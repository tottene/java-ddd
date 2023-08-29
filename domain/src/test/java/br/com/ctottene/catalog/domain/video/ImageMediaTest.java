package br.com.ctottene.catalog.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImageMediaTest {

    @Test
    public void givenValidParams_whenCallsNewImage_shoulderReturnInstance() {
        // given
        final var expectedChecksum = "abc";
        final var expectedName = "abc.jpg";
        final var expectedLocation = "/abc";

        // when
        final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        // then
        Assertions.assertNotNull(actualImage);
        Assertions.assertEquals(expectedChecksum, actualImage.checksum());
        Assertions.assertEquals(expectedName, actualImage.name());
        Assertions.assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_shoulderReturnTrue() {
        // given
        final var expectedChecksum = "abc";
        final var expectedLocation = "/abc";

        final var actualImage1 = ImageMedia.with(expectedChecksum, "Random", expectedLocation);
        final var actualImage2 = ImageMedia.with(expectedChecksum, "Simple", expectedLocation);

        // then
        Assertions.assertEquals(actualImage1, actualImage2);
        Assertions.assertNotSame(actualImage1, actualImage2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shoulderReturnError() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with(null, "random", "/images"));

        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with("abc", null, "/images"));

        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with("abc", "random", null));
    }
}
