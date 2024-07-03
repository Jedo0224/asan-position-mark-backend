package org.asanpositioningserver.domain.position.repository;

import org.asanpositioningserver.domain.position.entity.WatchLive;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchLiveRepository extends CrudRepository<WatchLive, Long> {
    List<WatchLive> findAllByLive(boolean live);

}