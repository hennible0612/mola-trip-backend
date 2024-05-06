package com.mola.domain.member.service;

import com.mola.domain.member.entity.Member;
import com.mola.domain.member.repository.MemberRepository;
import com.mola.domain.member.dto.LoginResponseDto;
import com.mola.domain.member.dto.OAuthMemberDto;
import com.mola.domain.member.entity.LoginProvider;
import com.mola.global.security.service.JwtProvider;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final JwtProvider jwtProvider;

    private final Map<String, OAuthService> oauthServices;

    private final MemberRepository memberRepository;

    public OAuthMemberDto processAuth(LoginProvider providerName, String authCode) {
        OAuthService oAuthService = oauthServices.get(
                StringUtils.uncapitalize(providerName.getServiceClass().getSimpleName()));

        if (oAuthService == null) {
            throw new IllegalArgumentException("Unsupported login provider: " + providerName.getProvider());
        }

        String token = oAuthService.getOAuthToken(authCode);

        return oAuthService.getOAuthUser(token);
    }

    @Transactional
    public LoginResponseDto findOrSaveMember(OAuthMemberDto oAuthMemberDto) {

        Member member = memberRepository.findByPersonalId(oAuthMemberDto.getPersonalId())
                .orElseGet(() -> saveMember(oAuthMemberDto));

        return jwtProvider.createTokens(member.getId());
    }

    private Member saveMember(OAuthMemberDto oAuthMemberDto) {
        Member newMember = Member.builder()
                .nickname(oAuthMemberDto.getNickname())
                .profileImageUrl(oAuthMemberDto.getProfileImageUrl())
                .personalId(oAuthMemberDto.getPersonalId()).build();

        memberRepository.save(newMember);
        return newMember;
    }
}
