package com.mola.domain.trip.service;

import com.mola.domain.trip.dto.NewTripSpotDto;
import com.mola.domain.trip.entity.TripPlan;
import com.mola.domain.trip.entity.TripSpot;
import com.mola.domain.trip.exception.TripErrorCode;
import com.mola.domain.trip.exception.TripException;
import com.mola.domain.trip.repository.TripPlanRepository;
import com.mola.domain.trip.repository.TripSpotRepository;
import com.mola.domain.trip.repository.TripStatus;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripSpotService {
    private final TripSpotRepository tripSpotRepository;
    private final TripPlanRepository tripPlanRepository;

    @Transactional
    public TripSpot createTripSpot(NewTripSpotDto tripSpot){
        TripPlan tripPlan = getTripPlan(tripSpot.getTripPlanId());

        Optional<List<TripSpot>> tripSpots = tripSpotRepository.findByTripIdAndStatus(tripPlan.getId(),
                TripStatus.INACTIVE);

        TripSpot newTripSpot = newTriptSpotDtoToTripSpot(tripPlan, tripSpots, tripSpot);

        tripSpotRepository.save(newTripSpot);

        return newTripSpot;
    }

    private TripSpot newTriptSpotDtoToTripSpot(TripPlan tripPlan, Optional<List<TripSpot>>  tripSpots, NewTripSpotDto tripSpot) {

        int order = 1;
        if(tripSpots.isPresent()){
            order = tripSpots.get().size();
        }

        return TripSpot.builder()
                .tripPlan(tripPlan)
                .addressName(tripSpot.getAddressName())
                .order(order)
                .categoryGroupName(tripSpot.getCategoryGroupName())
                .phone(tripSpot.getPhone())
                .placeUrl(tripSpot.getPlaceUrl())
                .roadAddressName(tripSpot.getRoadAddressName())
                .longitude(tripSpot.getX())
                .latitude(tripSpot.getY())
                .status(TripStatus.INACTIVE)
                .build();
    }

    private TripPlan getTripPlan(Long tripPlanId){
        return tripPlanRepository.findById(tripPlanId).orElseThrow(() -> new TripException(TripErrorCode.TripNotFound));
    }
}
