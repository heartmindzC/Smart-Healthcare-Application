package com.example.ehrservice.dto;

import com.example.ehrservice.model.EHR;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EHRResponse {
    private boolean status;
    private String message;
    private EHR result;
}

