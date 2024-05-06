package com.mola.domain.member.controller;

import com.mola.domain.member.dto.LoginResponseDto;
import com.mola.domain.member.dto.OAuthMemberDto;
import com.mola.domain.member.entity.LoginProvider;
import com.mola.domain.member.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/member/oauth")
    public ResponseEntity<LoginResponseDto> handleOauthLogin(@RequestParam("login-provider") String loginProvider,
                                                            @RequestParam("auth-code") String authCode) {
        OAuthMemberDto oAuthMemberDto = memberAuthService.processAuth(LoginProvider.toLoginProvider(loginProvider), authCode);
        LoginResponseDto loginResponseDto = memberAuthService.findOrSaveMember(oAuthMemberDto);

        return ResponseEntity.ok(loginResponseDto);
    }
}
