package com.mola.global.exception;

import com.mola.domain.trip.exception.TripException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> customExceptionHandler(CustomException e){
        ErrorResponseDto response = new ErrorResponseDto(e.getErrorCode().getHttpStatus(),
                e.getErrorCode().getErrorMessage());

        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(TripException.class)
    public ResponseEntity<ErrorResponseDto> handleTripException(TripException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getErrorCode().getHttpStatus(), e.getErrorCode().getErrorMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

}
