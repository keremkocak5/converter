package com.giftandgo.converter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    RESTRICTED_ISP("I0001", "ISP is restricted.", HttpStatus.FORBIDDEN),
    RESTRICTED_COUNTRY("I0002", "Country is restricted.", HttpStatus.FORBIDDEN),
    IP_API_CONNECTION_ERROR("I0010", "Could not connect to IP-API.", HttpStatus.INTERNAL_SERVER_ERROR),
    IP_API_RESOLVE_ERROR("I0011", "IP API could not resolve arguments.", HttpStatus.INTERNAL_SERVER_ERROR),
    CANNOT_READ_FILE("I0013", "Cannot read file.", HttpStatus.BAD_REQUEST),
    CANNOT_WRITE_FILE("I0014", "Cannot write file.", HttpStatus.INTERNAL_SERVER_ERROR),
    INCORRECT_FILE_FORMAT("I0020", "%s not valid at line %d", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("I0000", "Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
