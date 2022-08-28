
package bh.bhback.domain.place.repository;

import bh.bhback.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select p from Place p join fetch p.postList")
    Optional<Place> findByContentId(Long contentId);

    @Query("select p from Place p join fetch p.postList where p.contentId = :contentId")
    Optional<Place> findPlaceByContentId(Long contentId);
}
