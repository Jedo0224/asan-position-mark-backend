package org.asanpositioningserver.domain.position.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import org.asanpositioningserver.domain.position.entity.WatchLive;
import org.asanpositioningserver.domain.position.repository.WatchLiveRepository;

import java.util.List;
import java.util.stream.Collectors;

@Builder(access = AccessLevel.PRIVATE)
public record WatchLiveResponseDto(
        Long watchId
) {
    public static WatchLiveResponseDto of(Long watchId) {
        return WatchLiveResponseDto.builder()
                .watchId(watchId)
                .build();
    }

    public static List<WatchLiveResponseDto> liveListOf(List<WatchLive> watchLiveList) {
        return watchLiveList.stream()
                .map(watchLive -> WatchLiveResponseDto.of(watchLive.getId()))
                .collect(Collectors.toList());
    }
}
