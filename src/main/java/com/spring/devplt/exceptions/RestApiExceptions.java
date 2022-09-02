package com.spring.devplt.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiExceptions extends RuntimeException{
    private final ErrorCode errorCode;

}