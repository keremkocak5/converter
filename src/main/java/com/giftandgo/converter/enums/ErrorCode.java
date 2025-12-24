package com.giftandgo.converter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    RESTRICTED_COUNTRY("I0001", "This country is restricted.", HttpStatus.FORBIDDEN),
    RESTRICTED_ISP("I0002", "This ISP is restricted.", HttpStatus.FORBIDDEN),
    IP_API_CONNECTION_ERROR("I0003", "Could not connect to IP-API.", HttpStatus.INTERNAL_SERVER_ERROR),
    IP_API_RESOLVE_ERROR("I0004", "IP API could not resolve address.", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("I0000", "Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
