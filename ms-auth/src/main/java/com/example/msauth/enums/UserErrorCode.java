//package com.example.msauth.enums;
//
//import com.imran.exception.ErrorCode;
//import org.springframework.http.HttpStatus;
//
//public enum UserErrorCode implements ErrorCode {
//    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "user.not_found");
//
//    private final String code;
//    private final HttpStatus status;
//    private final String messageKey;
//
//    UserErrorCode(String code, HttpStatus status, String messageKey) {
//        this.code = code;
//        this.status = status;
//        this.messageKey = messageKey;
//    }
//
//    @Override
//    public String getCode() {
//        return code;
//    }
//
//    @Override
//    public HttpStatus getHttpStatus() {
//        return status;
//    }
//
//    @Override
//    public String getMessageKey() {
//        return messageKey;
//    }
//}
