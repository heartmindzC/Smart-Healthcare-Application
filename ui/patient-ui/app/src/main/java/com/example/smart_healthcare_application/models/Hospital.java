package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;

public class Hospital {

    @SerializedName("hospitalId")
    private String hospitalId;

    @SerializedName("hospitalName")
    private String hospitalName;

    @SerializedName("hospitalAddress")
    private String hospitalAddress;

    @SerializedName("hospitalPhone")
    private String hospitalPhone;

    public Hospital() {}

    public Hospital(String hospitalId, String hospitalName, String hospitalAddress, String hospitalPhone) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.hospitalPhone = hospitalPhone;
    }

    public String getHospitalId() { return hospitalId; }
    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getHospitalAddress() { return hospitalAddress; }
    public void setHospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; }

    public String getHospitalPhone() { return hospitalPhone; }
    public void setHospitalPhone(String hospitalPhone) { this.hospitalPhone = hospitalPhone; }

    @Override
    public String toString() {
        return hospitalName != null ? hospitalName : "";
    }
}
