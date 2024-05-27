package com.mola.domain.trip.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mola.domain.trip.dto.NewTripPlanDto;
import com.mola.domain.trip.dto.TripPlanDto;
import com.mola.domain.trip.dto.TripPlanInfoDto;
import com.mola.domain.trip.service.TripPlanService;
import com.mola.global.sse.TripPlanSseRegistry;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TripPlanController.class)
class TripPlanControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TripPlanSseRegistry sseRegistry;

    @MockBean
    TripPlanService tripPlanService;

    @DisplayName("새 여행 계획 생성")
    @WithMockUser
    @Test
    void createTripPlan() throws Exception {
        // given
        NewTripPlanDto newTripPlanDto = NewTripPlanDto.builder()
                .tripName("Test Trip")
                .startDate(LocalDateTime.of(2024, 5, 25, 0, 0))
                .endDate(LocalDateTime.of(2024, 5, 30, 0, 0))
                .build();

        // when
        when(tripPlanService.addTripPlan(any(NewTripPlanDto.class))).thenReturn(1L);

        // then
        mockMvc.perform(post("/api/trip-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTripPlanDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));
    }

    @DisplayName("계획에 사용자 추가 성공")
    @WithMockUser
    @Test
    void addMemberToTripSuccess() throws Exception {
        // given
        String tripCode = "12345";

        // when
        when(tripPlanService.addParticipant(tripCode)).thenReturn(1L);


        // then
        mockMvc.perform(post("/api/trip-plan/{tripCode}", tripCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));

        verify(tripPlanService, times(1)).addParticipant(tripCode);
    }

    @DisplayName("여행 계획 조회 성공")
    @WithMockUser
    @Test
    void getTripPlanSuccess() throws Exception {
        // given
        Long tripPlanId = 1L;
        TripPlanInfoDto mockTripPlanInfoDto = new TripPlanInfoDto();
        List<TripPlanDto> mockTripPlans = new ArrayList<>();
        mockTripPlanInfoDto.setTripPlanDtos(mockTripPlans);

        // when
        when(tripPlanService.getTripList(tripPlanId)).thenReturn(mockTripPlanInfoDto);
        when(tripPlanService.getTripPlans()).thenReturn(mockTripPlans);

        // then
        mockMvc.perform(get("/api/trip-plan/{tripPlanId}", tripPlanId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockTripPlanInfoDto)));

        verify(tripPlanService, times(1)).getTripList(tripPlanId);
        verify(tripPlanService, times(1)).getTripPlans();
    }

    @DisplayName("여행 계획 목록 조회 성공")
    @WithMockUser
    @Test
    void getTripPlanListSuccess() throws Exception {
        // given
        List<TripPlanDto> mockTripPlans = new ArrayList<>();

        TripPlanDto tripPlanDto1 = TripPlanDto.builder()
                .tripName("tripPlanDto1")
                .tripId(1L)
                .totalTripMember(3L)
                .tripImageUrl("naver.com")
                .build();

        TripPlanDto tripPlanDto2 = TripPlanDto.builder()
                .tripName("tripPlanDto2")
                .tripId(2L)
                .totalTripMember(3L)
                .tripImageUrl("naver.com")
                .build();
        
        mockTripPlans.add(tripPlanDto1);
        mockTripPlans.add(tripPlanDto2);

        // when
        when(tripPlanService.getTripPlans()).thenReturn(mockTripPlans);

        // then
        mockMvc.perform(get("/api/trip-plan/lists"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockTripPlans)));

        verify(tripPlanService, times(1)).getTripPlans();
    }

}