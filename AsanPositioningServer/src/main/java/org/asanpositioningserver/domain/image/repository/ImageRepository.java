package org.asanpositioningserver.domain.image.repository;

import org.asanpositioningserver.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
