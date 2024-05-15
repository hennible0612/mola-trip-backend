package com.mola.domain.member.dto;

import com.mola.domain.member.entity.LoginProvider;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OAuthMemberDto {
    private String nickname;
    private String personalId;
    private String profileImageUrl;
    private LoginProvider loginProvider;
}