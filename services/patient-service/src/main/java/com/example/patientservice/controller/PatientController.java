package com.example.patientservice.controller;

import com.example.patientservice.dto.response.ApiResponse;
import com.example.patientservice.model.Patient;
import com.example.patientservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.patientservice.dto.request.PatientRequest;
import com.example.patientservice.dto.request.UpdatePatientRequest;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/{patientId}")
    public ApiResponse<Patient> findPatientByPatientId(@PathVariable String patientId) {
        Patient patient = patientService.findById(patientId);
        return ApiResponse.<Patient>builder()
                .result(patient)
                .build();
    }
    
    @GetMapping("/userId/{userId}")
    public ApiResponse<Patient> findPatientByUserId(@PathVariable String userId) {
        Patient patient = patientService.findByUserId(userId);
        return ApiResponse.<Patient>builder()
                .result(patient)
                .build();
    }
    
    @PutMapping("/{userId}")
    public ApiResponse<Patient> updatePatientInfo(
            @PathVariable String userId,
            @RequestBody UpdatePatientRequest request) {
        Patient updatedPatient = patientService.updatePatientInfoByUserId(userId, request);
        return ApiResponse.<Patient>builder()
                .result(updatedPatient)
                .build();
    }
    @PostMapping("/")
    public ApiResponse<Patient> createPatient(@RequestBody PatientRequest request) {
        Patient patient = patientService.createPatient(request);
        return ApiResponse.<Patient>builder()
                .result(patient)
                .build();
//        PatientResponse response = new PatientResponse();
//        try {
//            Patient createdPatient = patientService.createPatient(request);
//            response.setStatus(true);
//            response.setMessage("Patient created successfully");
//            response.setResult(createdPatient);
//            return ResponseEntity.ok(response);
//        } catch (RuntimeException e) {
//            // Log lỗi để debug
//            System.err.println("Error creating patient: " + e.getMessage());
//            e.printStackTrace();
//
//            response.setStatus(false);
//            response.setMessage(e.getMessage() != null ? e.getMessage() : "Failed to create patient");
//            response.setResult(null);
//            // Trả về 200 OK với status = false để nhất quán với user service
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            // Catch tất cả exceptions khác (DataIntegrityViolationException, etc.)
//            System.err.println("Unexpected error creating patient: " + e.getMessage());
//            e.printStackTrace();
//
//            response.setStatus(false);
//            String errorMessage = "Failed to create patient";
//            if (e.getMessage() != null) {
//                errorMessage = e.getMessage();
//            }
//            response.setMessage(errorMessage);
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
    }

    @GetMapping("/")
    public ApiResponse<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        return ApiResponse.<List<Patient>>builder()
                .result(patients)
                .build();
    }
}
