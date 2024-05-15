package com.mola.domain.member.dto.oauthInfo;

import com.mola.domain.member.entity.LoginProvider;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getPersonalId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("name").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("picture").toString();
    }

    @Override
    public LoginProvider getLoginProvider() {
        return LoginProvider.GOOGLE;
    }
}
