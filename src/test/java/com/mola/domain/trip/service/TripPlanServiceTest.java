package com.mola.domain.trip.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mola.domain.member.entity.Member;
import com.mola.domain.trip.dto.NewTripPlanDto;
import com.mola.domain.trip.dto.TripPlanDto;
import com.mola.domain.trip.entity.TripPlan;
import com.mola.domain.trip.repository.TripPlanRepository;
import com.mola.domain.trip.repository.TripStatus;
import com.mola.domain.tripFriends.TripFriends;
import com.mola.domain.tripFriends.TripFriendsRepository;
import com.mola.global.exception.CustomException;
import com.mola.global.util.SecurityUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TripPlanServiceTest {

    @Mock
    private TripPlanRepository tripPlanRepository;

    @Mock
    private TripFriendsRepository tripFriendsRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private TripPlanService tripPlanService;

    private Member mockMember;

    private NewTripPlanDto mockTripPlanDto;

    private TripPlan mockTripPlan;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder()
                .id(1L)
                .build();
        mockTripPlanDto = NewTripPlanDto.builder()
                .tripName("SSAFY 방학 제발")
                .startDate(LocalDateTime.of(2024, 5, 25, 0, 0))
                .endDate(LocalDateTime.of(2024, 5, 30, 0, 0))
                .build();
        mockTripPlan = TripPlan.builder()
                .id(1L)
                .startDate(mockTripPlanDto.getStartDate())
                .endDate(mockTripPlanDto.getEndDate())
                .tripName(mockTripPlanDto.getTripName())
                .tripCode(UUID.randomUUID().toString())
                .tripStatus(TripStatus.ACTIVE)
                .build();
    }

    @DisplayName("TripPlan 생성시 TripPlan, TripFriend save가 호출되었는지 확인")
    @Test
    void addTripPlanSuccessfully() {
        // given
        TripPlan tripPlan = TripPlan.builder()
                .id(1L)
                .build();

        when(securityUtil.findCurrentMember()).thenReturn(mockMember);
        when(tripPlanRepository.save(any())).thenReturn(tripPlan);
        // when
        tripPlanService.addTripPlan(mockTripPlanDto);

        // then
        verify(tripPlanRepository).save(any(TripPlan.class));
        verify(tripFriendsRepository).save(any(TripFriends.class));
    }

    @DisplayName("TripPlan 생성시 TripPlan 저장값 확인")
    @Test
    void saveTripPlanSuccessfully() {
        // given
        TripPlan tripPlan = TripPlan.builder()
                .startDate(mockTripPlanDto.getStartDate())
                .endDate(mockTripPlanDto.getEndDate())
                .tripName(mockTripPlanDto.getTripName())
                .tripCode(UUID.randomUUID().toString())
                .tripStatus(TripStatus.ACTIVE)
                .build();

        when(tripPlanRepository.save(any(TripPlan.class))).thenReturn(tripPlan);

        // when
        TripPlan savedTripPlan = tripPlanRepository.save(tripPlan);

        // then
        assertNotNull(savedTripPlan);
        assertEquals(savedTripPlan.getTripCode(), tripPlan.getTripCode());
        assertEquals(savedTripPlan.getEndDate(), tripPlan.getEndDate());
        assertEquals(savedTripPlan.getStartDate(), tripPlan.getStartDate());
        verify(tripPlanRepository).save(tripPlan);
    }

    @DisplayName("TripPlan에 참가자 추가시 save 호출 확인 ")
    @Test
    void addParticipantSuccessfully() {
        // given
        String tripCode = UUID.randomUUID().toString();
        TripPlan tripPlan = TripPlan.builder()
                .startDate(mockTripPlanDto.getStartDate())
                .endDate(mockTripPlanDto.getEndDate())
                .tripName(mockTripPlanDto.getTripName())
                .tripCode(UUID.randomUUID().toString())
                .tripStatus(TripStatus.ACTIVE)
                .totalTripMember(1L)
                .build();

        // when
        when(securityUtil.findCurrentMember()).thenReturn(mockMember);
        when(tripPlanRepository.findByTripCode(tripCode)).thenReturn(Optional.of(tripPlan));
        tripPlanService.addParticipant(tripCode);

        // then
        verify(tripPlanRepository).findByTripCode(tripCode);
        verify(tripFriendsRepository).save(any(TripFriends.class));
    }

    @DisplayName("TripPlan 속한 유저면 id반환하고 저장을 하지 않는다.")
    @Test
    void addParticipantSuccessfullyWhenAlreadyInTripPlan() {
        // given
        String tripCode = UUID.randomUUID().toString();
        TripFriends tripFriends = TripFriends.builder()
                .tripPlan(mockTripPlan)
                .isOwner(false)
                .member(mockMember).build();
        // when
        when(securityUtil.findCurrentMember()).thenReturn(mockMember);
        when(tripPlanRepository.findByTripCode(tripCode)).thenReturn(Optional.of(mockTripPlan));
        when(tripFriendsRepository.findByMemberAndTripPlan(mockMember.getId(), mockTripPlan.getId()))
                .thenReturn(Optional.of(new TripFriends(mockMember, mockTripPlan, false)));

        tripPlanService.addParticipant(tripCode);

        // then
        assertEquals(mockMember.getId(), tripFriends.getMember().getId());
        verify(tripPlanRepository, times(0)).save(mockTripPlan);
        verify(tripFriendsRepository, times(0)).save(tripFriends);
    }


    @DisplayName("해당 tripCode 가 존재하지 않을시 예외를 던짐")
    @Test
    void addParticipantThrowsExceptionIfTripPlanNotFound() {
        String tripCode = UUID.randomUUID().toString();

        when(tripPlanRepository.findByTripCode(tripCode)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> tripPlanService.addParticipant(tripCode));
    }

    @DisplayName("성공적으로 TripPlan를 가지고 온다.")
    @Test
    void getCurrentUserTripPlans_Successful(){
        // given
        List<TripPlanDto> tripPlanDtos = new ArrayList<>();
        // when
        when(securityUtil.findCurrentMemberId()).thenReturn(1L);
        when(securityUtil.existMember(mockMember.getId()))
                .thenReturn(true);
        when(tripPlanRepository.getTripPostDtoByMemberId(mockMember.getId()))
                .thenReturn(tripPlanDtos);

        tripPlanService.getTripPlans();
        // then
        verify(tripPlanRepository, times(1)).getTripPostDtoByMemberId(mockMember.getId());
    }

    @DisplayName("성공적으로 TripPlan를 가지고 오지 않는다.")
    @Test
    void getCurrentUserTripPlans_Fail(){
        // given
        List<TripPlanDto> tripPlanDtos = new ArrayList<>();
        // when
        when(securityUtil.findCurrentMemberId()).thenReturn(1L);
        when(securityUtil.existMember(mockMember.getId()))
                .thenReturn(false);

        // then
        assertThrows(CustomException.class, () -> tripPlanService.getTripPlans());
        verify(tripPlanRepository, times(0)).getTripPostDtoByMemberId(mockMember.getId());
    }

//    @DisplayName("성공적으로 TripPlan를 가지고 오지 않는다.")
//    @Test
//    void checkMemberIsInTrip() {
//
//        // given
//        TripFriends tripFriends = TripFriends.builder()
//                .tripPlan(mockTripPlan)
//                .isOwner(false)
//                .member(mockMember).build();
//        // when
//        when(securityUtil.findCurrentMemberId()).thenReturn(mockMember.getId());
//        when(tripPlanRepository.findById(mockTripPlan.getId())).thenReturn(Optional.ofNullable(mockTripPlan));
//        when(tripFriendsRepository.findByMemberAndTripPlan(mockMember.getId(), mockTripPlan.getId())).thenReturn(Optional.ofNullable(tripFriends));
//
//        // then
//        assertEquals(mockTripPlan, );
//
//
//    }
}