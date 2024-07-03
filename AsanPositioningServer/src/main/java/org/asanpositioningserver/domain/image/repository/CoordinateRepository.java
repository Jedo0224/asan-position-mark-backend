package org.asanpositioningserver.domain.image.repository;


import org.asanpositioningserver.domain.image.entity.Coordinate;
import org.asanpositioningserver.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Coordinate findByImageIdAndPosition(Image image, String position);

    Coordinate findByPosition(String position);
    List<Coordinate> findAllByImageId(Optional<Image> image);
}
