package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;

public class Department {

    @SerializedName("departmentId")
    private String departmentId;

    @SerializedName("departmentName")
    private String departmentName;

    @SerializedName("hospitalId")
    private String hospitalId;

    public Department() {}

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getHospitalId() { return hospitalId; }
    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }

    @Override
    public String toString() {
        return departmentName != null ? departmentName : "";
    }
}
