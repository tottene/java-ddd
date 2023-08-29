package br.com.ctottene.catalog.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AudioVideoMediaTest {

    @Test
    public void givenValidParams_whenCallsNewAudioVideo_shoulderReturnInstance() {
        // given
        final var expectedChecksum = "abc";
        final var expectedName = "abc.jpg";
        final var expectedRawLocation = "/abc";
        final var expectedEncodedLocation = "/abc-encoded";
        final var expectedStatus = MediaStatus.COMPLETED;

        // when
        final var actualAudioVideoMedia = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);

        // then
        Assertions.assertNotNull(actualAudioVideoMedia);
        Assertions.assertEquals(expectedChecksum, actualAudioVideoMedia.checksum());
        Assertions.assertEquals(expectedName, actualAudioVideoMedia.name());
        Assertions.assertEquals(expectedRawLocation, actualAudioVideoMedia.rawLocation());
        Assertions.assertEquals(expectedEncodedLocation, actualAudioVideoMedia.encodedLocation());
        Assertions.assertEquals(expectedStatus, actualAudioVideoMedia.status());
    }

    @Test
    public void givenTwoAudioVideosWithSameChecksumAndLocation_whenCallsEquals_shoulderReturnTrue() {
        // given
        final var expectedChecksum = "abc";
        final var expectedRawLocation = "/abc";

        final var actualVideoMedia1 = AudioVideoMedia.with(expectedChecksum, "video1", expectedRawLocation, "encoded1", MediaStatus.PROCESSING);
        final var actualVideoMedia2 = AudioVideoMedia.with(expectedChecksum, "video2", expectedRawLocation, "encoded2", MediaStatus.COMPLETED);

        // then
        Assertions.assertEquals(actualVideoMedia1, actualVideoMedia2);
        Assertions.assertNotSame(actualVideoMedia1, actualVideoMedia2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shoulderReturnError() {
        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with(null, "random", "/videos", "encoded", MediaStatus.COMPLETED));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("abc", null, "/images", "encoded", MediaStatus.COMPLETED));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("abc", "random", null, "encoded", MediaStatus.COMPLETED));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("abc", "random", "images", null, MediaStatus.COMPLETED));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("abc", "random", "images", "encoded", null));
    }
}
