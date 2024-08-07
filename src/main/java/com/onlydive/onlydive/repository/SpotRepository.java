package com.onlydive.onlydive.repository;

import com.onlydive.onlydive.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
    Optional<Spot> findByName(String name);

    List<Spot> findAllByLatitudeBetweenAndLongitudeBetween(Double minLatitude, Double maxLatitude,
                                                           Double minLongitude, Double maxLongitude);
}
