package com.clothes.noc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import com.clothes.noc.dto.response.ApiResponse;

import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(AppException.class)
    private ResponseEntity<ApiResponse<Map<String, Object>>> handlerAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.<Map<String, Object>>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        if (!exception.getAttributes().isEmpty()) {
            apiResponse.setBody(exception.getAttributes());
        }

        return ResponseEntity.status(errorCode.getStatus()).body(apiResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ApiResponse<Void>> handlerAccessDeniedException(AccessDeniedException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(ErrorCode.ACCESS_DENIED.getCode())
                .message(ErrorCode.ACCESS_DENIED.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getStatus()).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiResponse<Void>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String key = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(key);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatus()).body(apiResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    private ResponseEntity<ApiResponse<String>> handlerNoResourceFoundException(NoResourceFoundException exception) {
        ErrorCode errorCode = ErrorCode.NO_RESOURCE_FOUND;
        return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.<String>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .body(exception.getResourcePath())
                .build());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    private void handlerMissingRequestCookieException(MissingRequestCookieException exception) {
        log.error(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiResponse<Void>> handlerException(Exception exception) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(ErrorCode.UNCATEGORIZED_ERROR.getCode())
                .message(ErrorCode.UNCATEGORIZED_ERROR.getMessage())
                .build();
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_ERROR.getStatus()).body(apiResponse);
    }


}
