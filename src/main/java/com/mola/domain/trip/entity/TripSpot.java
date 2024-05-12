package com.mola.domain.trip.entity;

import com.mola.domain.trip.repository.TripStatus;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class TripSpot {

    @Id
    @GeneratedValue
    @Column(name = "trip_spot_id")
    private Long id;

    @Column(name = "trip_seq")
    private Integer order;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "category_group_name")
    private String categoryGroupName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "place_url")
    private String placeUrl;

    @Column(name = "road_address_name")
    private String roadAddressName;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @JoinColumn(name = "trip_plan_id")
    @ManyToOne
    private TripPlan tripPlan;

}
