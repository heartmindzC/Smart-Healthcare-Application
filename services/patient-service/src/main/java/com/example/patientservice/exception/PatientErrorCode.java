package com.example.patientservice.exception;

import com.example.common_exception.ErrorCode;
import org.springframework.http.HttpStatusCode;

public enum PatientErrorCode implements ErrorCode {
    ;

    private int code;
    private String message;
    private HttpStatusCode status;

    PatientErrorCode(int code, String message, HttpStatusCode status){
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
