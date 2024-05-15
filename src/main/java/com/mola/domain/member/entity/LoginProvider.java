package com.mola.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum LoginProvider {
    KAKAO,
    NAVER,
    GOOGLE,
    GITHUB;

}
