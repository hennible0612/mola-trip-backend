package com.mola.domain.member;

import com.mola.domain.trip.TripPlan;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@IdClass(TripFriendsId.class)
@Builder
@Setter
@Getter
@Entity
public class TripFriends {
    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Id
    @ManyToOne
    @JoinColumn(name = "trip_plan_id")
    private TripPlan tripPlan;
    private boolean isOwner;
}
