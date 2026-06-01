package com.example.smart_healthcare_application.api.request;

public class UpdateUserRequest {
    private String phone;
    private String email;
    private String fullname;
    private String address;
    private String birth;
    private String gender;

    public UpdateUserRequest() {}

    public UpdateUserRequest(String phone, String email, String fullname, String address, String birth, String gender) {
        this.phone = phone;
        this.email = email;
        this.fullname = fullname;
        this.address = address;
        this.birth = birth;
        this.gender = gender;
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
}
