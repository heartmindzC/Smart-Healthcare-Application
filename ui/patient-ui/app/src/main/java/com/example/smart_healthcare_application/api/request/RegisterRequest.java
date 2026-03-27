package com.example.smart_healthcare_application.api.request;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class RegisterRequest {
    @SerializedName("userId")
    private String userId;

    @SerializedName("password")
    private String password;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("address")
    private String address;

    // Sử dụng String cho ngày tháng để dễ dàng truyền định dạng ISO 8601 (VD: "2026-03-22T15:48:03.222Z")
    // Retrofit/Gson sẽ tự động map chuỗi này qua Backend chuẩn xác
    @SerializedName("birth")
    private String birth;

    // Giới tính gửi lên dạng chuỗi (VD: "MALE", "FEMALE")
    @SerializedName("gender")
    private String gender;

    @SerializedName("roles")
    private Set<String> roles;

    public RegisterRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
