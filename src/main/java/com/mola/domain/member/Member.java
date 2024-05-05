package com.mola.domain.member;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    @Column(name = "nickname", length = 100, nullable = false)
    private String nickname;
    @Column(name = "personal_id", nullable = false)
    private String personalId;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @OneToMany(
            mappedBy = "member",
            cascade = CascadeType.ALL
    )
    List<TripFriends> tripFriendsList;
    @Column(name = "refresh_token")
    private String refreshToken;
}
