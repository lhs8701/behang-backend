package bh.bhback.domain.image.repository;

import bh.bhback.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
}
