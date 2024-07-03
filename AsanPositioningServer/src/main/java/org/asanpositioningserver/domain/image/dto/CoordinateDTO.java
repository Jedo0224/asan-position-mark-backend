package org.asanpositioningserver.domain.image.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CoordinateDTO {
    private Long imageId;
    private Long coordinateId;
    private String latitude;
    private String longitude;
    private String position;
    @Column(precision = 10, scale = 4)
    private BigDecimal startX;
    @Column(precision = 10, scale = 4)
    private BigDecimal startY;
    @Column(precision = 10, scale = 4)
    private BigDecimal endX;
    @Column(precision = 10, scale = 4)
    private BigDecimal endY;
}
