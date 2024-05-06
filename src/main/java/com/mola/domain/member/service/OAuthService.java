package com.mola.domain.member.service;

import com.mola.domain.member.dto.OAuthMemberDto;

public interface OAuthService {
    String getOAuthToken(String authorizationCode);
    OAuthMemberDto getOAuthUser(String accessToken);
}
