package org.asanpositioningserver.domain.position.mongorepository;

import lombok.RequiredArgsConstructor;
import org.asanpositioningserver.domain.position.entity.Position;
import org.asanpositioningserver.domain.position.entity.PositionData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;

@RequiredArgsConstructor
public class PositionCustomRepositoryImpl implements PositionCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updatePosition(Long watchId, PositionData positionData) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("date").is(LocalDate.now())
                .and("watchId").is(watchId));
        update.addToSet("positionDataList", positionData);
        mongoTemplate.updateFirst(query, update, Position.class);
    }
}
