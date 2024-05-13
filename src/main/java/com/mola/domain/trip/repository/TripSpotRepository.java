package com.mola.domain.trip.repository;

import com.mola.domain.trip.entity.TripSpot;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripSpotRepository extends JpaRepository<TripSpot, Long> {

    Optional<List<TripSpot>> findByTripIdAndStatus(Long tripId, TripStatus status);
}
