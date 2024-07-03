package org.asanpositioningserver.domain.position.repository;

import org.asanpositioningserver.domain.position.dto.request.BeaconCountsDTO;
import org.asanpositioningserver.domain.position.entity.BeaconData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeaconDataRepository extends JpaRepository<BeaconData,Long> {
    List<BeaconData> findAllByPosition(String positionName);


//    @Query("SELECT b.position, c.longitude, c.latitude, COUNT(b) FROM Coordinate c LEFT JOIN BeaconData b ON b.position = c.position GROUP BY b.position ,  c.longitude, c.latitude")
//    List<Object[]> findAllBeaconCount();

    @Query("SELECT c.position, COUNT(b) FROM Coordinate c LEFT JOIN BeaconData b ON b.position = c.position GROUP BY c.position")
    List<Object[]> findAllBeaconCount();


    public void deleteAllByImageId(Long imageId);

    public void deleteAllByPosition(String positionName);
}
