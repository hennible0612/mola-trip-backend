package com.mola.domain.trip.entity;

import com.mola.domain.tripFriends.TripFriends;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class TripPlan {

    @Id @GeneratedValue
    @Column(name = "trip_plan_id")
    private Long id;

    @Column(name = "trip_name", length = 100, nullable = false)
    private String tripName;

    @Column(name = "trip_start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "trip_end_date", nullable = false)
    private LocalDateTime endDate;
    @OneToMany(
            mappedBy = "tripPlan",
            cascade = CascadeType.ALL
    )

    private List<TripFriends> tripFriendsList;

    @OneToMany(
            mappedBy = "tripPlan",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private List<TripSpot> TripSpotList;
}
