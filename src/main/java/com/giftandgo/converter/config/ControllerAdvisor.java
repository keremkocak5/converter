package com.giftandgo.converter.config;

import com.giftandgo.converter.exception.ConverterRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConverterRuntimeException.class)
    protected ProblemDetail handleConverterRuntimeException(ConverterRuntimeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                e.getErrorCode().getHttpStatus(),
                e.getMessage()
        );
        problemDetail.setProperty("errorCode", e.getErrorCode().getErrorCode());
        problemDetail.setProperty("title", e.getErrorCode().getErrorMessage());
        return problemDetail;
    }

    @ExceptionHandler(value = {Exception.class})
    protected ProblemDetail handleException(Exception e) {
        log.error("Unexpected exception! ", e);
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "Internal Server Error");
    }

}
