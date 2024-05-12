package com.mola.domain.trip.exception;

import com.mola.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum TripErrorCode implements ErrorCode {
    TripNotFound(HttpStatus.NOT_FOUND, "해당 여행 계획을 찾을 수 없습니다."),
    TripAccessDenied(HttpStatus.FORBIDDEN, "이 여행 계획에 접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}