package br.com.ctottene.catalog.domain.video;

import br.com.ctottene.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video video);

    Video update(Video video);

    Optional<Video> findById(VideoID id);

    void deleteById(VideoID id);

    Pagination<Video> findAll(VideoSearchQuery query);
}
