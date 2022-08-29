
package bh.bhback.domain.place.repository;

import bh.bhback.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceJpaRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByContentId(Long contentId);
}
