package com.example.smart_healthcare_application.api.request;

import com.google.gson.annotations.SerializedName;

public class PatientRequest {
    @SerializedName("userId")
    private String userId;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("birth")
    private String birth;

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

    public PatientRequest() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
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
}
