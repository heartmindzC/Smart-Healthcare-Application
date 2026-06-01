package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;

public class Appointment {

    @SerializedName("appointmentId")
    private String appointmentId;

    @SerializedName("doctorId")
    private String doctorId;

    @SerializedName("doctorName")
    private String doctorName;

    @SerializedName("patientId")
    private String patientId;

    @SerializedName("patientName")
    private String patientName;

    @SerializedName("hospitalId")
    private String hospitalId;

    @SerializedName("hospitalName")
    private String hospitalName;

    @SerializedName("departmentId")
    private String departmentId;

    @SerializedName("departmentName")
    private String departmentName;

    @SerializedName("timeSlotId")
    private String timeSlotId;

    @SerializedName("appointmentDateTime")
    private String appointmentDateTime; // Khuyên dùng String để an toàn khi parse JSON

    @SerializedName("status")
    private String status;

    @SerializedName("notes")
    private String notes;

    @SerializedName("reason")
    private String reason;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    // --- Constructor rỗng (Cần thiết cho Firebase/Gson) ---
    public Appointment() {
    }

    // --- Getters & Setters ---

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getHospitalId() { return hospitalId; }
    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getTimeSlotId() { return timeSlotId; }
    public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }

    public String getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(String appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}