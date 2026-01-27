package com.example.doctorservice.dto.request;

import com.example.doctorservice.model.Gender;
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
    @NotNull(message = "USER_ID_NULL")
    @Pattern(regexp = "^\\d{12}$", message = "IDENTITY_NUMBER_INVALID")
    private String userId;
    @NotEmpty(message = "HOSPITAL_ID_NULL")
    private String hospitalId;
    @NotEmpty(message = "DEPARTMENT_NULL")
    private String department;
    @NotEmpty(message = "NAME_NULL")
    private String fullName;
    @NotNull(message = "BIRTH_NULL")
    private Date birth;
    @NotNull(message = "GENDER_NULL")
    private Gender gender;
    @NotEmpty(message = "LICENSE_ID_NULL")
    private String licenseId;
}
