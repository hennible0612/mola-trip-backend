package com.mola.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OAuthMemberDto {
    private String nickname;
    private String personalId;
    private String profileImageUrl;
}