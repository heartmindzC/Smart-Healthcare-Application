package com.example.smart_healthcare_application.api.request;

public class UpdateDoctorRequest {
    private String hospitalId;
    private String department;
    private String fullName;
    private String birth;
    private String gender;
    private String licenseId;

    public UpdateDoctorRequest() {
    }

    public UpdateDoctorRequest(String hospitalId, String department, String fullName, String birth, String gender, String licenseId) {
        this.hospitalId = hospitalId;
        this.department = department;
        this.fullName = fullName;
        this.birth = birth;
        this.gender = gender;
        this.licenseId = licenseId;
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

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }
}
