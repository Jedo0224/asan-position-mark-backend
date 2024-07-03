package org.asanpositioningserver.domain.position.mongorepository;

import org.asanpositioningserver.domain.position.entity.PositionData;

public interface PositionCustomRepository {
    void updatePosition(final Long watchId, final PositionData positionData);
}
