package com.mola.domain.member.service;

import com.mola.domain.member.dto.KakaoTokenRequestDto;
import com.mola.domain.member.dto.KakaoTokenResponseDto;
import com.mola.domain.member.dto.KakaoUserDto;
import com.mola.domain.member.dto.OAuthMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class KakaoOAuthServiceImpl implements OAuthService {

    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestApiKey;

    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    @Value("${KAKAO_TOKEN_REQUEST_URI}")
    private String kakaoTokenRequestUri;

    @Value("${KAKAO_USERINFO_REQUEST_URI}")
    private String kakaoUserInfoRequestUri;

    private final RestClient restClient;

    @Override
    public String getOAuthToken(String authorizationCode) {
        KakaoTokenRequestDto kakaoTokenRequestDto = new KakaoTokenRequestDto("authorization_code", kakaoRestApiKey, kakaoRedirectUri, authorizationCode);
        MultiValueMap<String, String> params = kakaoTokenRequestDto.toMultiValueMap();

        KakaoTokenResponseDto kakaoTokenResponseDto = restClient.post()
                .uri(kakaoTokenRequestUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(KakaoTokenResponseDto.class);

        return kakaoTokenResponseDto.getAccessToken();
    }

    @Override
    public OAuthMemberDto getOAuthUser(String accessToken) {
        System.out.println("getOAuthUser");
        System.out.println("getOAuthUser");
        KakaoUserDto kakaoUserDto = restClient.get()
                .uri(kakaoUserInfoRequestUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KakaoUserDto.class);

        return kakaoUserToMemberDto(kakaoUserDto);
    }

    public static OAuthMemberDto kakaoUserToMemberDto(KakaoUserDto kakaoUser) {
        return OAuthMemberDto.builder()
                .nickname(kakaoUser.getProperties().getNickname())
                .personalId(String.valueOf(kakaoUser.getId()))
                .profileImageUrl(kakaoUser.getProperties().getProfileImage())
                .build();
    }

}
