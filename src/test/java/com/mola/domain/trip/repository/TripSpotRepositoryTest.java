package com.mola.domain.trip.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TripSpotRepositoryTest {
    @Autowired
    TripSpotRepository tripSpotRepository;

    @Autowired
    TripPlanRepository tripPlanRepository;

    @Test
    @DisplayName("")

}