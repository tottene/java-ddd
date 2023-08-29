package br.com.ctottene.catalog.application.video.create;

import br.com.ctottene.catalog.domain.video.Video;

public record CreateVideoOutput(String id) {
    public static CreateVideoOutput from(final String id) {
        return new CreateVideoOutput(id);
    }

    public static CreateVideoOutput from(final Video video) {
        return new CreateVideoOutput(video.getId().getValue());
    }
}
