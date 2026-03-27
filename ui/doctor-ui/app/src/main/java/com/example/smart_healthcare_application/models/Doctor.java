package com.example.smart_healthcare_application.models;

import java.io.Serializable;
import java.util.Date;

public class Doctor implements Serializable {
    private String doctorId;
    private String userId;
    private String hospitalId;
    private String department;
    private String fullName;
    private Date birth;
    private Date registrationAt;
    private String gender;
    private String licenseId;
    private Boolean isActive;

    public Doctor() {
    }

    public Doctor(String doctorId, String userId, String hospitalId, String department, String fullName, Date birth, Date registrationAt, String gender, String licenseId, Boolean isActive) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.hospitalId = hospitalId;
        this.department = department;
        this.fullName = fullName;
        this.birth = birth;
        this.registrationAt = registrationAt;
        this.gender = gender;
        this.licenseId = licenseId;
        this.isActive = isActive;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Date getRegistrationAt() {
        return registrationAt;
    }

    public void setRegistrationAt(Date registrationAt) {
        this.registrationAt = registrationAt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
