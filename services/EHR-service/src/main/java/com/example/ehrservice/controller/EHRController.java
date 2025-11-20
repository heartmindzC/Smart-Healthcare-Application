package com.example.ehrservice.controller;

import com.example.ehrservice.dto.EHRResponse;
import com.example.ehrservice.service.EHRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ehr")
public class EHRController {
    
    @Autowired
    private EHRService ehrService;
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<EHRResponse> getEHRByPatientId(@PathVariable Integer patientId) {
        try {
            EHRResponse ehr = ehrService.getEHRByPatientId(patientId);
            return ResponseEntity.ok(ehr);
        } catch (Exception e) {
            System.err.println("Error in getEHRByPatientId: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<EHRResponse> getEHRByUserId(@PathVariable String userId) {
        try {
            EHRResponse ehr = ehrService.getEHRByUserId(userId);
            return ResponseEntity.ok(ehr);
        } catch (Exception e) {
            System.err.println("Error in getEHRByUserId: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


