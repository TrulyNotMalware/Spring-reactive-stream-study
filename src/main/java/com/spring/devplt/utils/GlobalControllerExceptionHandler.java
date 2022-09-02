package com.spring.devplt.utils;

import com.spring.devplt.exceptions.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(RestApiExceptions.class)
    public @ResponseBody
    Mono<ResponseEntity<ErrorResponse>> handleResourceNotFoundExceptions(ServerHttpRequest request, final RestApiExceptions e){
        final ErrorCode errorCode = CommonErrorCode.RESOURCE_NOT_FOUND;
        return handleException(errorCode);
    }

    private ErrorResponse createErrorResponse(ErrorCode errorCode){
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage()).build();
    }

    private Mono<ResponseEntity<ErrorResponse>> handleException(ErrorCode errorCode){
        return Mono.just(ResponseEntity.status(errorCode.getHttpStatus())
                .body(createErrorResponse(errorCode)));
    }
}
