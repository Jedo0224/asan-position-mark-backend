package org.asanpositioningserver.domain.position.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BeaconData {
    @Id
    @GeneratedValue
    private int id;
    private Long imageId;
    private String position;
    @Column(columnDefinition = "json")
    private String beaconData;
}
