package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

public class User {

    @SerializedName("userId")
    private String userId;

    @SerializedName("phone")
    private String phone;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("address")
    private String address;

    @SerializedName("birth")
    private Date birth;

    @SerializedName("email")
    private String email;

    @SerializedName("gender")
    private String gender;

    @SerializedName("roles")
    private List<String> roles;

    // --- Getters ---
    public String getUserId() { return userId; }
    public String getPhone() { return phone; }
    public String getFullname() { return fullname; }
    public String getAddress() { return address; }
    public Date getBirth() { return birth; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public List<String> getRoles() { return roles; }

    // --- Setters ---
    public void setUserId(String userId) { this.userId = userId; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public void setAddress(String address) { this.address = address; }
    public void setBirth(Date birth) { this.birth = birth; }
    public void setEmail(String email) { this.email = email; }
    public void setGender(String gender) { this.gender = gender; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
