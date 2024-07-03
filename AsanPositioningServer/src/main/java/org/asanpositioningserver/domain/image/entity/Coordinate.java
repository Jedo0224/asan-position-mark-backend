package org.asanpositioningserver.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "coordinate")
@Entity
public class Coordinate {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name="imageId")
    private Image imageId;
    private String position;
    private String latitude;
    private String longitude;
    @Column(precision = 10, scale = 4)
    private BigDecimal startX;
    @Column(precision = 10, scale = 4)
    private BigDecimal  startY;
    @Column(precision = 10, scale = 4)
    private BigDecimal  endX;
    @Column(precision = 10, scale = 4)
    private BigDecimal  endY;
}
