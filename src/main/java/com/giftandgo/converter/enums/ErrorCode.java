package com.giftandgo.converter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    RESTRICTED_ISP("I0001", "This ISP is restricted.", HttpStatus.FORBIDDEN),
    RESTRICTED_COUNTRY("I0002", "This country is restricted.", HttpStatus.FORBIDDEN),
    INVALID_FILE_FORMAT("I0010", "Invalid file format.", HttpStatus.BAD_REQUEST),
    IP_API_CONNECTION_ERROR("I0011", "Could not connect to IP-API.", HttpStatus.INTERNAL_SERVER_ERROR),
    IP_API_RESOLVE_ERROR("I0012", "IP API could not resolve arguments.", HttpStatus.INTERNAL_SERVER_ERROR),
    CANNOT_READ_FILE("I0013", "Cannot read file.", HttpStatus.BAD_REQUEST),
    NO_FILE_VALIDATION_STRATEGY_FOUND("I0014", "No file validation strategy set.", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("I0000", "Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
