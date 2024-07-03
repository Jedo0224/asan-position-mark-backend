package org.asanpositioningserver.domain.position.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record PositionResponseDto(
        Long watchId,
        String watchName,
        LocalDateTime currentTime,
        String position
) {
    public static PositionResponseDto of(Long watchId,String watchName,String date) {
        return PositionResponseDto.builder()
                .watchId(watchId)
                .watchName(watchName)
                .currentTime(LocalDateTime.now())
                .position(date)
                .build();
    }
}
