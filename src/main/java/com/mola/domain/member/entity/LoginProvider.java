package com.mola.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginProvider {
    KAKAO,
    NAVER,
    GOOGLE,
    GITHUB;


}
