package com.mola.domain.trip.entity;

import com.mola.domain.trip.controller.TripStatus;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class TripSpot {

    @Id @GeneratedValue
    @Column(name = "trip_spot_id")

    private Long id;
    @Column(name = "trip_seq")

    private Integer order;
    @Enumerated(EnumType.STRING)

    private TripStatus status;
    @JoinColumn(name = "trip_plan_id")

    @ManyToOne
    private TripPlan tripPlan;
}
