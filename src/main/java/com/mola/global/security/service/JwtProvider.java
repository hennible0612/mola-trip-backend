package com.mola.global.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.mola.domain.member.Member;
import com.mola.domain.member.MemberRepository;
import com.mola.global.security.dto.LoginMemberResponse;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Value("${JWT_ACCESS_TOKEN_TIME}")
    private Long accessTokenExpireTime;

    @Value("${JWT_REFRESH_TOKEN_TIME}")
    private Long refreshTokenExpireTime;

    private static final String ISSUER = "MolaTrip";

    private final MemberRepository memberRepository;

    public String createAccessToken(Long memberId) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject("AccessToken")
                .withExpiresAt(new Date(now.getTime() + accessTokenExpireTime))
                .withClaim("memberId", memberId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject("RefreshToken")
                .withClaim("memberId", memberId)
                .withJWTId(UUID.randomUUID().toString())
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpireTime))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public LoginMemberResponse createTokens(Long memberId) {
        String accessToken = createAccessToken(memberId);
        String refreshToken = createRefreshToken(memberId);

        return LoginMemberResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void updateRefreshToken(Long memberId, String refreshToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No member found"));
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);
    }

    public Long extractMemberIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("memberId").asLong();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public UserDetails createUserDetails(Long memberId, String role) {
        return User.builder()
                .username(memberId.toString())
                .password("")
                .authorities(role)
                .build();
    }
}