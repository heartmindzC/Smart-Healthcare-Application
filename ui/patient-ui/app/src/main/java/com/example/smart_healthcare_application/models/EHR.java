package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EHR {
    @SerializedName("patientInfo")
    private Patient patientInfo;

    @SerializedName("medicalHistory")
    private List<MedicalVisits> medicalHistory;

    @SerializedName("allPrescriptions")
    private List<Prescription> allPrescriptions;

    public Patient getPatientInfo() { return patientInfo; }
    public void setPatientInfo(Patient patientInfo) { this.patientInfo = patientInfo; }

    public List<MedicalVisits> getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(List<MedicalVisits> medicalHistory) { this.medicalHistory = medicalHistory; }

    public List<Prescription> getAllPrescriptions() { return allPrescriptions; }
    public void setAllPrescriptions(List<Prescription> allPrescriptions) { this.allPrescriptions = allPrescriptions; }
}