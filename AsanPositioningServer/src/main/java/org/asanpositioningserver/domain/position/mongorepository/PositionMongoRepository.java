package org.asanpositioningserver.domain.position.mongorepository;

import org.asanpositioningserver.domain.position.entity.Position;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface PositionMongoRepository extends MongoRepository<Position, String>, PositionCustomRepository{
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);
    Position findOneByWatchIdAndDate(Long watchId, LocalDate date);
}
