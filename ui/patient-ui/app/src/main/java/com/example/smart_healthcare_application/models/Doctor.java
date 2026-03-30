package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;

public class Doctor {

    @SerializedName("doctorId")
    private String doctorId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("hospitalId")
    private String hospitalId;

    @SerializedName("department")
    private String department;

    @SerializedName("birth")
    private String birth;

    @SerializedName("registrationAt")
    private String registrationAt;

    @SerializedName("gender")
    private String gender;

    @SerializedName("licenseId")
    private String licenseId;

    @SerializedName("isActive")
    private Boolean isActive;

    public Doctor() {}

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getHospitalId() { return hospitalId; }
    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getBirth() { return birth; }
    public void setBirth(String birth) { this.birth = birth; }

    public String getRegistrationAt() { return registrationAt; }
    public void setRegistrationAt(String registrationAt) { this.registrationAt = registrationAt; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLicenseId() { return licenseId; }
    public void setLicenseId(String licenseId) { this.licenseId = licenseId; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
