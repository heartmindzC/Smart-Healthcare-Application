package com.example.appointmentservice.exception;

import com.example.common_exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum AppointmentErrorCode implements ErrorCode {
    NOT_FOUND(3001, "Appointment not found", HttpStatus.NOT_FOUND),
    APPOINTMENT_ID_NULL(3002, "Appointment ID is required", HttpStatus.BAD_REQUEST),
    DOCTOR_ID_NULL(3003, "Doctor ID is required", HttpStatus.BAD_REQUEST),
    DOCTOR_NAME_NULL(3004, "Doctor name is required", HttpStatus.BAD_REQUEST),
    PATIENT_ID_NULL(3005, "Patient ID is required", HttpStatus.BAD_REQUEST),
    PATIENT_NAME_NULL(3006, "Patient name is required", HttpStatus.BAD_REQUEST),
    HOSPITAL_ID_NULL(3007, "Hospital ID is required", HttpStatus.BAD_REQUEST),
    HOSPITAL_NAME_NULL(3008, "Hospital name is required", HttpStatus.BAD_REQUEST),
    DEPARTMENT_ID_NULL(3009, "Department ID is required", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NAME_NULL(3010, "Department name is required", HttpStatus.BAD_REQUEST),
    TIME_SLOT_ID_NULL(3011, "Time slot ID is required", HttpStatus.BAD_REQUEST),
    APPOINTMENT_DATE_TIME_NULL(3012, "Appointment date time is required", HttpStatus.BAD_REQUEST),
    APPOINTMENT_DATE_TIME_INVALID(3013, "Appointment date time must be in the future", HttpStatus.BAD_REQUEST),
    STATUS_NULL(3014, "Status is required", HttpStatus.BAD_REQUEST),
    STATUS_INVALID(3015, "Status is invalid", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION(3016, "Invalid status transition", HttpStatus.BAD_REQUEST),
    ;

    private int code;
    private String message;
    private HttpStatusCode status;

    AppointmentErrorCode(int code, String message, HttpStatusCode status) {
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
