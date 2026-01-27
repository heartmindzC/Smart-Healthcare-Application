package com.example.doctorservice.exception;

import com.example.common_exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum DoctorErrorCode implements ErrorCode {
    NOT_FOUND(2001, "Not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXIST(2002, "User already exist", HttpStatus.CONFLICT),
    IDENTITY_NUMBER_INVALID(2003, "Identity number invalid", HttpStatus.BAD_REQUEST),
    USER_ID_NULL(2004, "User ID is required", HttpStatus.BAD_REQUEST),
    HOSPITAL_ID_NULL(2005, "Hospital ID is required", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NULL(2006, "Department is required", HttpStatus.BAD_REQUEST),
    NAME_NULL(2007, "Name is required", HttpStatus.BAD_REQUEST),
    BIRTH_NULL(2008, "Birth date is required", HttpStatus.BAD_REQUEST),
    GENDER_NULL(2009, "Gender is required", HttpStatus.BAD_REQUEST),
    LICENSE_ID_NULL(2010, "License ID is required", HttpStatus.BAD_REQUEST),
    DOCTOR_ID_NULL(2011, "Doctor ID is required", HttpStatus.BAD_REQUEST),
    DAY_OF_WEEK_NULL(2012, "Day of week is required", HttpStatus.BAD_REQUEST),
    START_TIME_NULL(2013, "Start time is required", HttpStatus.BAD_REQUEST),
    END_TIME_NULL(2014, "End time is required", HttpStatus.BAD_REQUEST),
    IS_AVAILABLE_NULL(2015, "Is available is required", HttpStatus.BAD_REQUEST),
    SPECIFIC_DATE_NULL(2016, "Specific date is required", HttpStatus.BAD_REQUEST),

    ;

    private int code;
    private String message;
    private HttpStatusCode status;

    DoctorErrorCode(int code, String message, HttpStatusCode status) {
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
