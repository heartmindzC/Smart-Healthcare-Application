package com.example.smart_healthcare_application.api.request;

public class UpdatePatientRequest {
    private String insuranceId;
    private String emergencyCallingNumber;
    private String bloodType;
    private Double heights;
    private Double weights;

    public UpdatePatientRequest() {}

    public UpdatePatientRequest(String insuranceId, String emergencyCallingNumber, String bloodType, Double heights, Double weights) {
        this.insuranceId = insuranceId;
        this.emergencyCallingNumber = emergencyCallingNumber;
        this.bloodType = bloodType;
        this.heights = heights;
        this.weights = weights;
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
