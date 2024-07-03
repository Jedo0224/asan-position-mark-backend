package org.asanpositioningserver.domain.position.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionDTO {
    private Long imageId;
    private Long coordinateId;
    private String position;
}
