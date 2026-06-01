package com.example.common_exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final ErrorCodeRegistry errorCodeRegistry;

    public GlobalExceptionHandler(ErrorCodeRegistry errorCodeRegistry) {
        this.errorCodeRegistry = errorCodeRegistry;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleException(Exception e){
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder()
                        .code(9999)
                        .message("Undefined Error")
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleException(MethodArgumentNotValidException e){
        e.printStackTrace();

        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = errorCodeRegistry.getErrorCode(enumKey);

        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AppException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleException(AppException e){
        e.printStackTrace();

        return ResponseEntity.status(e.errorCode.getStatus())
                .body(ApiResponse.builder()
                        .code(e.errorCode.getCode())
                        .message(e.errorCode.getMessage())
                        .build());
    }

}
