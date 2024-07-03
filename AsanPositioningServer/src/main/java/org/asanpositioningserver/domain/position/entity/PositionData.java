package org.asanpositioningserver.domain.position.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PositionData {
    private String position;
    private String timeStamp;

    public static PositionData of(String position) {
        return PositionData.builder()
                .position(position)
                .timeStamp(String.valueOf(System.currentTimeMillis() / 1000))
                .build();
    }
}
