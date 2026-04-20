package com.example.patientservice.service;


import com.example.patientservice.dto.PatientRequest;
import com.example.patientservice.dto.UpdatePatientRequest;
import com.example.patientservice.model.BloodType;
import com.example.patientservice.model.Gender;
import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepo patientRepo;

    public Optional<Patient> findByPatientId(Integer patientId){
        return patientRepo.findByPatientId(patientId);
    }
    public Optional<Patient> findByUserId(Integer userId){
        return patientRepo.findByUserId(userId);
    }
    public Optional<List<Patient>> findByFullName(String fullName){
        return patientRepo.findByFullName(fullName);
    }

    
    public List<Patient> findAll(){
        return patientRepo.findAll();
    }
    
    public Patient findById(int id) {
        return patientRepo.findById(id).get();
    }
    
    public Patient findByUserId(String userId) {
        return patientRepo.findByUserId(userId);
    }
    
    public Patient updatePatientInfoByUserId(String userId, UpdatePatientRequest request) {
        Patient patient = patientRepo.findByUserId(userId);
        
        if (patient == null) {
            throw new RuntimeException("Patient not found with userId: " + userId);
        }
        
        // Cập nhật các trường từ request
        if (request.getInsuranceId() != null) {
            patient.setInsuranceId(request.getInsuranceId());
        }
        if (request.getEmergencyCallingNumber() != null) {
            patient.setEmergencyCallingNumber(request.getEmergencyCallingNumber());
        }
        if (request.getJob() != null) {
            patient.setJob(request.getJob());
        }
        if (request.getBloodType() != null) {
            patient.setBloodType(request.getBloodType());
        }
        if (request.getHeights() != null) {
            patient.setHeights(request.getHeights());
        }
        if (request.getWeights() != null) {
            patient.setWeights(request.getWeights());
        }
        
        return patientRepo.save(patient);
    }
    public Patient createPatient(PatientRequest request) {
        // Kiểm tra xem patient đã tồn tại với userId này chưa
        Patient existingPatient = patientRepo.findByUserId(request.getUserId());
        if (existingPatient != null) {
            throw new RuntimeException("Patient already exists with userId: " + request.getUserId());
        }
        
        // Convert String birth to Date
        Date birthDate = null;
        if (request.getBirth() != null && !request.getBirth().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                birthDate = sdf.parse(request.getBirth());
            } catch (ParseException e) {
                throw new RuntimeException("Invalid birth date format. Expected: yyyy-MM-dd");
            }
        }
        
        // Tạo patient mới
        Patient newPatient = new Patient();
        newPatient.setUserId(request.getUserId());
        newPatient.setFullName(request.getFullName());
        newPatient.setBirth(birthDate);
        
        // Convert String gender to Gender enum
        if (request.getGender() != null) {
            try {
                newPatient.setGender(Gender.valueOf(request.getGender().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid gender: " + request.getGender());
            }
        }
        
        newPatient.setInsuranceId(request.getInsuranceId());
        newPatient.setEmergencyCallingNumber(request.getEmergencyCallingNumber());
        newPatient.setJob(request.getJob());
        
        // Convert String bloodType to BloodType enum
        if (request.getBloodType() != null) {
            try {
                newPatient.setBloodType(BloodType.valueOf(request.getBloodType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid blood type: " + request.getBloodType());
            }
        }
        
        newPatient.setHeights(request.getHeights());
        newPatient.setWeights(request.getWeights());
        newPatient.setRegisteredAt(new Date());
        newPatient.setIsActive(true); // Set isActive = true
        
        try {
            Patient savedPatient = patientRepo.save(newPatient);
            System.out.println("Patient saved successfully with ID: " + savedPatient.getPatientId());
            return savedPatient;
        } catch (Exception e) {
            System.err.println("Error saving patient to database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save patient: " + e.getMessage(), e);
        }
    }
}
