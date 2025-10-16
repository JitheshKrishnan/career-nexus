package com.careernexus.common.exceptions;

public class CustomException extends RuntimeException {
    private String errorCode;
    private int httpStatus;

    public CustomException(String message) {
        super(message);
        this.httpStatus = 500;
    }

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = 500;
    }

    public CustomException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = 500;
    }

    public CustomException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = 500;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}