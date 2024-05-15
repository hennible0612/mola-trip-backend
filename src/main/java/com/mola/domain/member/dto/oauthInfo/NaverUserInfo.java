package com.mola.domain.member.dto.oauthInfo;


import com.mola.domain.member.entity.LoginProvider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");;
    }

    @Override
    public String getPersonalId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProfileImageUrl() {
        return (String) attributes.get("profile_image");
    }

    @Override
    public LoginProvider getLoginProvider() {
        return LoginProvider.NAVER;
    }
}
