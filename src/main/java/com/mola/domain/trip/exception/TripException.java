package com.mola.domain.trip.exception;

import com.mola.global.exception.CustomException;

public class TripException extends CustomException {
    public TripException(TripErrorCode errorCode) {
        super(errorCode);
    }
}
