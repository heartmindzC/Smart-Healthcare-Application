package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;

public class Patient {
    @SerializedName("patientId")
    private String patientId;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("userId")
    private String userId;

    @SerializedName("birth")
    private String birth;

    @SerializedName("registeredAt")
    private String registeredAt;

    @SerializedName("gender")
    private String gender;

    @SerializedName("insuranceId")
    private String insuranceId;

    @SerializedName("emergencyCallingNumber")
    private String emergencyCallingNumber;

    @SerializedName("job")
    private String job;

    @SerializedName("bloodType")
    private String bloodType;

    @SerializedName("heights")
    private Double heights;

    @SerializedName("weights")
    private Double weights;

    @SerializedName("isActive")
    private Boolean isActive;

    public Patient(String patientId, String fullName, String userId, String birth, String registeredAt, String gender, String insuranceId, String emergencyCallingNumber, String job, String bloodType, Double heights, Double weights, Boolean isActive) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.userId = userId;
        this.birth = birth;
        this.registeredAt = registeredAt;
        this.gender = gender;
        this.insuranceId = insuranceId;
        this.emergencyCallingNumber = emergencyCallingNumber;
        this.job = job;
        this.bloodType = bloodType;
        this.heights = heights;
        this.weights = weights;
        this.isActive = isActive;
    }

    public Patient() {}

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getEmergencyCallingNumber() {
        return emergencyCallingNumber;
    }

    public void setEmergencyCallingNumber(String emergencyCallingNumber) {
        this.emergencyCallingNumber = emergencyCallingNumber;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Double getHeights() {
        return heights;
    }

    public void setHeights(Double heights) {
        this.heights = heights;
    }

    public Double getWeights() {
        return weights;
    }

    public void setWeights(Double weights) {
        this.weights = weights;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
