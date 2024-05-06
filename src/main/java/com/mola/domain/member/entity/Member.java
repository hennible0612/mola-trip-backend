package com.mola.domain.member.entity;

import com.mola.domain.tripFriends.TripFriends;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "refresh_token", length = 512)
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
