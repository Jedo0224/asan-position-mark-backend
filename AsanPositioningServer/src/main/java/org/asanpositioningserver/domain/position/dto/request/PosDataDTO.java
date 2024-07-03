package org.asanpositioningserver.domain.position.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PosDataDTO(
        String watchId,
        String position,
        String password,
        @JsonProperty("beacon_data")
        List<BeaconDataDTO> beaconData
) {
}
