package com.mola.domain.trip.service;


import static org.mockito.Mockito.when;

import com.mola.domain.trip.dto.NewTripSpotDto;
import com.mola.domain.trip.entity.TripPlan;
import com.mola.domain.trip.entity.TripSpot;
import com.mola.domain.trip.repository.TripPlanRepository;
import com.mola.domain.trip.repository.TripSpotRepository;
import com.mola.domain.trip.repository.TripStatus;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TripSpotServiceTest {

    @Mock
    TripSpotRepository tripSpotRepository;

    @Mock
    TripPlanRepository tripPlanRepository;

    @InjectMocks
    TripSpotService tripSpotService;

    @BeforeEach
    void setUp() {
        tripSpotService = new TripSpotService(tripSpotRepository, tripPlanRepository);
    }

    @Test
    @Transactional
    @DisplayName("저장")
    void saveTripSpotWhenTripPlanExists() {
        // given
        TripPlan tripPlan = TripPlan.builder()
                .id(1L)
                .tripName("여행계획1")
                .endDate(LocalDateTime.MAX)
                .startDate(LocalDateTime.MIN)
                .build();

        NewTripSpotDto tripSpotDto = NewTripSpotDto.builder()
                .tripPlanId(1L)
                .placeName("tripSpotDto")
                .build();
        List<TripSpot> tripSpots = new ArrayList<>();
        TripSpot tripSpot = new TripSpot();
        Optional<List<TripSpot>> optionalTripSpots = Optional.of(tripSpots);

        when(tripPlanRepository.findById(1L)).thenReturn(Optional.ofNullable(tripPlan));
        when(tripSpotRepository.findByTripIdAndStatus(tripPlan.getId(), TripStatus.INACTIVE))
                .thenReturn(optionalTripSpots);
//        when(tripSpotRepository.save(tripSpotDto)).thenReturn(tripSpot);
        // when

        // then
        tripPlanRepository.save(tripPlan);

        Optional<TripPlan> tripPlanOptional = tripPlanRepository.findById(1L);
        TripPlan tripPlanSaved = tripPlanOptional.get();

//        System.out.println(tripPlanOptional);


    }

}