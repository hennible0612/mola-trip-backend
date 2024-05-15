package com.mola.domain.member.exception;

import com.mola.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {
    MemberNotFound(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다."),
    AccessDenied(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    DuplicateMember(HttpStatus.CONFLICT, "이미 존재하는 멤버입니다."),
    InvalidMemberData(HttpStatus.BAD_REQUEST, "멤버 데이터가 유효하지 않습니다."),
    MemberRegistrationFailed(HttpStatus.INTERNAL_SERVER_ERROR, "멤버 등록에 실패하였습니다."),
    MemberUpdateFailed(HttpStatus.INTERNAL_SERVER_ERROR, "멤버 정보 업데이트에 실패하였습니다."),
    MemberDeletionFailed(HttpStatus.INTERNAL_SERVER_ERROR, "멤버 삭제에 실패하였습니다."),
    UnsupportedLoginProvider(HttpStatus.BAD_REQUEST, "지원하지 않는 로그인 제공자입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}