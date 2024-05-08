package com.mola.domain.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mola.domain.member.dto.LoginResponseDto;
import com.mola.domain.member.dto.OAuthMemberDto;
import com.mola.domain.member.entity.LoginProvider;
import com.mola.domain.member.entity.Member;
import com.mola.domain.member.repository.MemberRepository;
import com.mola.global.security.service.JwtProvider;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberAuthServiceTest {

    @Mock
    JwtProvider jwtProvider;

    @Mock
    Map<String, OAuthService> oauthService;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberAuthService memberAuthService;

    @BeforeEach
    void setUp() {
        memberAuthService = new MemberAuthService(jwtProvider, oauthService, memberRepository);
    }

    @Test
    @DisplayName("유효한 Auth 일시 MemberDto를 반환")
    void whenValidAuthThenReturnMemberDto() {
        // given
        String authCode = "validOauthCode";
        String token = "validToken";
        OAuthMemberDto expectedUser = OAuthMemberDto.builder()
                .nickname("nickname")
                .personalId("1")
                .nickname("nickname")
                .build();
        OAuthService mockOAuthService = mock(OAuthService.class);

        when(oauthService.get(anyString())).thenReturn(mockOAuthService);
        when(mockOAuthService.getOAuthToken(authCode)).thenReturn(token);
        when(mockOAuthService.getOAuthUser(token)).thenReturn(expectedUser);

        // when
        OAuthMemberDto result = memberAuthService.processAuth(LoginProvider.KAKAO, authCode);

        // then
        assertNotNull(result);
        assertEquals(expectedUser.getNickname(), result.getNickname());

    }

    @Test
    @DisplayName("회원가입/로그인 성공시 토큰 반환")
    void whenLoginThenFindOrSaveMemberReturnToken() {
        // given
        OAuthMemberDto oAuthMemberDto = OAuthMemberDto.builder()
                .nickname("nickname")
                .personalId("123456")
                .nickname("nickname")
                .build();
        Member newMember = new Member();

        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .refreshToken("refreshToken")
                .accessToken("accessToken")
                .build();
        when(memberRepository.findByPersonalId(oAuthMemberDto.getPersonalId())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(newMember);
        when(jwtProvider.createTokens(newMember.getId())).thenReturn(loginResponse);

        // when
        LoginResponseDto result = memberAuthService.findOrSaveMember(oAuthMemberDto);

        // return
        assertNotNull(result);
        assertEquals(loginResponse.getAccessToken(), result.getAccessToken());
        assertEquals(loginResponse.getRefreshToken(), result.getRefreshToken());
    }
}