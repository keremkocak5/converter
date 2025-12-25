package com.giftandgo.converter.config;

import com.giftandgo.converter.exception.ConverterRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConverterRuntimeException.class})
    protected ProblemDetail handleConverterRuntimeException(ConverterRuntimeException e) {
        return ProblemDetail.forStatusAndDetail(e.getErrorCode().getHttpStatus(), e.getErrorCode().getErrorMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    protected ProblemDetail handleException(Exception e) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "Internal Server Error");
    }

}
