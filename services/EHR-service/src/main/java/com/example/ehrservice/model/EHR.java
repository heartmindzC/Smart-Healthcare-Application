package com.example.ehrservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EHR {
    private PatientInfo patientInfo;
    private List<MedicalVisitInfo> medicalHistory;
    private List<PrescriptionInfo> allPrescriptions;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientInfo {
        private Integer patientId;
        private String fullName;
        private String userId;
        private String birth;
        private String gender;
        private String bloodType;
        private Double heights;
        private Double weights;
        private String insuranceId;
        private String emergencyCallingNumber;
        private String job;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicalVisitInfo {
        private Long visitId;
        private String doctorName;
        private String doctorId;
        private Date visitDate;
        private String hospital;
        private String department;
        private String diagnosis;
        private List<PrescriptionInfo> prescriptions;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrescriptionInfo {
        private Long prescriptionId;
        private String medicationName;
        private String dosage;
        private String frequency;
        private Integer quantity;
        private String instructions;
        private Date prescribedDate;
    }
}


