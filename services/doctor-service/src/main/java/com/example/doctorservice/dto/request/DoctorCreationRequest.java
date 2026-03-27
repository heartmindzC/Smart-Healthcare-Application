package com.example.doctorservice.dto.request;

import com.example.doctorservice.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreationRequest {

    // ========== Thông tin User ==========
    @NotNull(message = "USER_ID_NULL")
    @Pattern(regexp = "^\\d{12}$", message = "IDENTITY_NUMBER_INVALID")
    private String userId;

    @NotEmpty(message = "PASSWORD_NULL")
    private String password;

    @Pattern(regexp = "^\\d{10}$", message = "PHONE_INVALID")
    private String phone;

    @Email(message = "EMAIL_INVALID")
    private String email;

    @NotEmpty(message = "FULL_NAME_NULL")
    private String fullName;

    @NotEmpty(message = "ADDRESS_NULL")
    private String address;

    @NotNull(message = "BIRTH_NULL")
    private Date birth;

    @NotNull(message = "GENDER_NULL")
    private Gender gender;

    // ========== Thông tin Doctor ==========
    @NotEmpty(message = "HOSPITAL_ID_NULL")
    private String hospitalId;

    @NotEmpty(message = "DEPARTMENT_NULL")
    private String department;

    @NotEmpty(message = "LICENSE_ID_NULL")
    private String licenseId;
}
