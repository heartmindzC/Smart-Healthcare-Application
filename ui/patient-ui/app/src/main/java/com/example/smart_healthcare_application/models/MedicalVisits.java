package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MedicalVisits {

    @SerializedName("visitId")
    private String visitId;

    @SerializedName("doctorName")
    private String doctorName;

    @SerializedName("doctorId")
    private String doctorId;

    @SerializedName("visitDate")
    private String visitDate;

    @SerializedName("hospital")
    private String hospital;

    @SerializedName("department")
    private String department;

    @SerializedName("diagnosis")
    private String diagnosis;

    @SerializedName("prescriptions")
    private List<Prescription> prescriptions;

    public MedicalVisits() {
    }

    public MedicalVisits(String visitId, String doctorName, String doctorId, String visitDate, String hospital, String department, String diagnosis, List<Prescription> prescriptions) {
        this.visitId = visitId;
        this.doctorName = doctorName;
        this.doctorId = doctorId;
        this.visitDate = visitDate;
        this.hospital = hospital;
        this.department = department;
        this.diagnosis = diagnosis;
        this.prescriptions = prescriptions;
    }

    public String getVisitId() { return visitId; }
    public void setVisitId(String visitId) { this.visitId = visitId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getVisitDate() { return visitDate; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }
}