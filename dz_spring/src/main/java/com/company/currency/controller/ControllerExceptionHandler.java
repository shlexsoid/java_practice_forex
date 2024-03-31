package com.company.currency.controller;

import com.company.currency.dto.ErrorResponse;
import com.company.currency.handle_errors.CurrencyDoesntExists;
import com.company.currency.handle_errors.NoInfo;
import java.util.Objects;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorResponse> handleException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .title(ex.getMessage())
                    .description(
                        Objects.nonNull(ex.getCause()) ? ex.getCause().toString() : StringUtils.EMPTY)
                    .build());
    }

    @ExceptionHandler(value = {CurrencyDoesntExists.class, NoInfo.class})
    protected ResponseEntity<ErrorResponse> handleCurrencyException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .title(ex.getMessage())
                                .description(ex.toString())
                                .build());
    }
}
