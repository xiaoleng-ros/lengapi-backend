package com.leng.lengapiclientsdk.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * 自定义异常类
 */
@Getter
public class ApiException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}