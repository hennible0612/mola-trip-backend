package com.mola.domain.member.entity;

import com.mola.domain.member.service.KakaoOAuthServiceImpl;
import com.mola.domain.member.service.OAuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginProvider {
    KAKAO("kakao", KakaoOAuthServiceImpl.class);

    private final String provider;

    private final Class<? extends OAuthService> serviceClass;

    public static LoginProvider toLoginProvider(String provider) {
        for (LoginProvider loginProvider : values()) {
            if (loginProvider.getProvider().equalsIgnoreCase(provider)) {
                return loginProvider;
            }
        }
        throw new IllegalArgumentException("No matching login provider found for: " + provider);
    }
}
