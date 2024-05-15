package com.mola.global.auth;

import static com.mola.domain.member.exception.MemberErrorCode.MemberRegistrationFailed;
import static com.mola.domain.member.exception.MemberErrorCode.UnsupportedLoginProvider;

import com.mola.domain.member.dto.OAuthMemberDto;
import com.mola.domain.member.entity.LoginProvider;
import com.mola.domain.member.entity.Member;
import com.mola.domain.member.exception.MemberException;
import com.mola.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        String oAuth2ClientName = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        OAuthMemberDto oAuthMemberDto = mapToOAuthMemberDto(oAuth2ClientName, oAuth2User);
        if (oAuthMemberDto != null) {

            Member member = findOrSaveMember(oAuthMemberDto);
            return new CustomOAuth2User(member);

        }
        throw new MemberException(MemberRegistrationFailed);

    }

    private OAuthMemberDto mapToOAuthMemberDto(String clientName, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if ("kakao".equals(clientName)) {
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            return OAuthMemberDto.builder()
                    .loginProvider(LoginProvider.KAKAO)
                    .personalId(attributes.get("id").toString())
                    .nickname((String) properties.get("nickname"))
                    .profileImageUrl((String) profile.get("profile_image_url"))
                    .build();
        }
        if ("naver".equals(clientName)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            return OAuthMemberDto.builder()
                    .loginProvider(LoginProvider.NAVER)
                    .personalId(response.get("id").toString())
                    .nickname((String) response.get("name"))
                    .profileImageUrl((String) response.get("profile_image"))
                    .build();
        }

        if ("google".equals(clientName)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            return OAuthMemberDto.builder()
                    .loginProvider(LoginProvider.GOOGLE)
                    .personalId(response.get("id").toString())
                    .nickname((String) response.get("name"))
                    .profileImageUrl((String) response.get("picture"))
                    .build();
        }
        throw new MemberException(UnsupportedLoginProvider);
    }

    public Member findOrSaveMember(OAuthMemberDto oAuthMemberDto) {
        return memberRepository.findByPersonalId(oAuthMemberDto.getPersonalId())
                .orElseGet(() -> saveMember(oAuthMemberDto));
    }

    private Member saveMember(OAuthMemberDto oAuthMemberDto) {
        Member newMember = Member.builder()
                .loginProvider(oAuthMemberDto.getLoginProvider())
                .personalId(oAuthMemberDto.getPersonalId())
                .nickname(oAuthMemberDto.getNickname())
                .profileImageUrl(oAuthMemberDto.getProfileImageUrl())
                .build();

        memberRepository.save(newMember);
        return newMember;
    }
}
