package com.spring.devplt.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST,"Invalid Parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
