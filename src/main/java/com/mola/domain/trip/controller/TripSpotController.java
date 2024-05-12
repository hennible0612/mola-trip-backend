package com.mola.domain.trip.controller;

import com.mola.domain.trip.dto.NewTripSpotDto;
import com.mola.domain.trip.service.TripSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TripSpotController {

    private final TripSpotService tripSpotService;

    @PostMapping("trip-spots")
    public ResponseEntity<String> addTripSpot(@RequestBody NewTripSpotDto newTripSpotDto){
        tripSpotService.createTripSpot(newTripSpotDto);

        // TODO : 저장 후 소켓전달
        return ResponseEntity.ok("success");

    }
}
