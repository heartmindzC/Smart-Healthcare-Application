package com.example.patientservice.controller;

import com.example.patientservice.model.Patient;
import com.example.patientservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @GetMapping("/profile/{patientId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer patientId){
        return patientService.findByPatientId(patientId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

import com.example.patientservice.dto.PatientRequest;
import com.example.patientservice.dto.PatientResponse;
import com.example.patientservice.dto.UpdatePatientRequest;
import com.example.patientservice.model.Patient;
import com.example.patientservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/get-patient/{patientId}")
    public ResponseEntity<Patient> findPatientByPatientId(@PathVariable int patientId) {
        Patient patient = patientService.findById(patientId);
        return ResponseEntity.ok(patient);
    }
    
    @GetMapping("/get-patient-by-user-id/{userId}")
    public ResponseEntity<Patient> findPatientByUserId(@PathVariable String userId) {
        Patient patient = patientService.findByUserId(userId);
        if (patient != null) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PutMapping("/update/{userId}")
    public ResponseEntity<Patient> updatePatientInfo(
            @PathVariable String userId,
            @RequestBody UpdatePatientRequest request) {
        try {
            Patient updatedPatient = patientService.updatePatientInfoByUserId(userId, request);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/create-patient")
    public ResponseEntity<PatientResponse> createPatient(@RequestBody PatientRequest request) {
        PatientResponse response = new PatientResponse();
        try {
            Patient createdPatient = patientService.createPatient(request);
            response.setStatus(true);
            response.setMessage("Patient created successfully");
            response.setResult(createdPatient);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Log lỗi để debug
            System.err.println("Error creating patient: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(false);
            response.setMessage(e.getMessage() != null ? e.getMessage() : "Failed to create patient");
            response.setResult(null);
            // Trả về 200 OK với status = false để nhất quán với user service
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Catch tất cả exceptions khác (DataIntegrityViolationException, etc.)
            System.err.println("Unexpected error creating patient: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(false);
            String errorMessage = "Failed to create patient";
            if (e.getMessage() != null) {
                errorMessage = e.getMessage();
            }
            response.setMessage(errorMessage);
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
}
