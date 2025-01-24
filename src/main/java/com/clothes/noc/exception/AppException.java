package com.clothes.noc.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class AppException extends RuntimeException{
    private final ErrorCode errorCode;
    private final transient Map<String, Object> attributes;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.attributes = new HashMap<>();
    }

    public AppException(ErrorCode errorCode, Map<String, Object> attributes) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.attributes = attributes;
    }

}
