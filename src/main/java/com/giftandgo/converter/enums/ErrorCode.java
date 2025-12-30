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
    INCORRECT_DELIMITERS("I0020", "Delimiters not correct at line %d", HttpStatus.BAD_REQUEST),
    INVALID_UUID("I0021", "UUID not valid at line %d", HttpStatus.BAD_REQUEST),
    INVALID_ID("I0022", "Id not valid at line %d", HttpStatus.BAD_REQUEST),
    INVALID_NAME("I0023", "Name not valid at line %d", HttpStatus.BAD_REQUEST),
    INVALID_LIKES("I0024", "Likes not valid at line %d", HttpStatus.BAD_REQUEST),
    INVALID_TRANSPORT("I0025", "Transport not valid at line %d", HttpStatus.BAD_REQUEST),
    INVALID_AVG_SPEED("I0026", "Average speed not valid at line %d", HttpStatus.BAD_REQUEST),
    INVALID_TOP_SPEED("I0027", "Top speed not valid at line %d", HttpStatus.BAD_REQUEST),
    EMPTY_LINE("I0028", "Empty line found at line %d", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("I0000", "Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
