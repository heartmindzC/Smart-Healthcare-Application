package com.example.common_exception;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorCodeRegistry {
    private final Map<String, ErrorCode> errorCodeMap = new HashMap<>();

    public void register(Class<? extends Enum<?>> enumClass) {
        for (Object item : enumClass.getEnumConstants()) {
            if (item instanceof ErrorCode code) {
                errorCodeMap.put(((Enum<?>) item).name(), code);
            }
        }
    }

    public ErrorCode getErrorCode(String key) {
        return errorCodeMap.get(key);
    }

}
