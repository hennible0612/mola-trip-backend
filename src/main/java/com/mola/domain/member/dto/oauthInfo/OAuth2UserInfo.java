package com.mola.domain.member.dto.oauthInfo;

import com.mola.domain.member.entity.LoginProvider;

public interface OAuth2UserInfo {
    String getPersonalId();
    String getNickname();
    String getProfileImageUrl();
    LoginProvider getLoginProvider();
}
