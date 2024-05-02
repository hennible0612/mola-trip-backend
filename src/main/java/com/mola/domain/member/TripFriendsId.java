package com.mola.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter @Setter
public class TripFriendsId implements Serializable {
    private Long member;
    private Long tripPlan;
}
