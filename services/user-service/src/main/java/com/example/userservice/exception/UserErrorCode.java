package com.example.userservice.exception;

import com.example.common_exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum UserErrorCode implements ErrorCode {
    PHONE_INVALID(1001, "Phone Number Invalid", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1002, "Email Address Invalid", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1003, "Not Found", HttpStatus.NOT_FOUND),
    PASSWORD_INVALID(1004, "Password Invalid", HttpStatus.BAD_REQUEST),
    LOGIN_METHOD_INVALID(1005, "Login Method Invalid", HttpStatus.BAD_REQUEST),
    IDENTITY_NUMBER_INVALID(1006, "Identity Number Invalid", HttpStatus.BAD_REQUEST),
    EMPTY_NAME(1007, "Name Is Not Empty", HttpStatus.BAD_REQUEST),
    NULL_NAME(1008, "Name Is Null", HttpStatus.BAD_REQUEST),
    EMPTY_PASSWORD(1009, "Password Is Empty", HttpStatus.BAD_REQUEST),
    EMPTY_ADDRESS(1010, "Address Is Empty", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED(1011, "Username or password is incorrect", HttpStatus.BAD_REQUEST),
    NULL_BIRTH(1012, "Birthday Is Null", HttpStatus.BAD_REQUEST),
    NULL_GENDER(1013, "Gender Is Null", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_NULL(1014, "Old Password Is Null", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_NULL(1015, "New Password Is Null", HttpStatus.BAD_REQUEST),
    ID_NULL(1016, "Id Is Null", HttpStatus.BAD_REQUEST),
    USERNAME_NULL(1017, "Username Is Null", HttpStatus.BAD_REQUEST),
    TYPE_NULL(1018, "Type Is Null", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1019, "Email Exists", HttpStatus.BAD_REQUEST),
    ID_EXISTS(1020, "Id Exists", HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1021, "Phone Exists", HttpStatus.BAD_REQUEST),
    SEND_OTP_FAILED(1022, "Send OTP Failed", HttpStatus.BAD_REQUEST),
    CONNECTION_REFUSE(1023, "Connection Refuse", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(1024, "OTP Expired", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1024, "OTP Invalid", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_RESET(1025, "Unauthorized Reset", HttpStatus.UNAUTHORIZED),;

    private int code;
    private String message;
    private HttpStatusCode status;

    UserErrorCode(int code, String message, HttpStatusCode status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatusCode getStatus() {
        return this.status;
    }
}
