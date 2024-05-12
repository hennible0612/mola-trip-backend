package com.mola.domain.trip.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewTripSpotDto {

    @JsonProperty("trip_plan_id")
    private Long tripPlanId;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("category_group_name")
    private String categoryGroupName;

    @JsonProperty("distance")
    private String distance;

    @JsonProperty("id")
    private String id;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("place_url")
    private String placeUrl;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    @JsonProperty("x")
    private String x;

    @JsonProperty("y")
    private String y;

}
