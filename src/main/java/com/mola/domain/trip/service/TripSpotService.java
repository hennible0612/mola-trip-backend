package com.mola.domain.trip.service;

import com.mola.domain.trip.dto.NewTripSpotDto;
import com.mola.domain.trip.exception.TripErrorCode;
import com.mola.domain.trip.repository.TripPlanRepository;
import com.mola.domain.trip.repository.TripSpotRepository;
import com.mola.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripSpotService {
    private final TripSpotRepository tripSpotRepository;
    private final TripPlanRepository tripPlanRepository;

    @Transactional
    public void createTripSpot(NewTripSpotDto tripSpot){
        System.out.println(tripSpot);
        System.out.println(tripSpot);
        validateTripPlan(tripSpot.getTripPlanId());
//        tripSpotRepository.save(tripSpot);

    }
    private void validateTripPlan(Long tripPlanId){
        if(!tripPlanRepository.existsById(tripPlanId)){
            throw new CustomException(TripErrorCode.TripNotFound);
        }
    }
}
