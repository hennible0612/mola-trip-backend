package com.mola.global.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginMemberResponse {
    private final String accessToken;
    private final String refreshToken;
}