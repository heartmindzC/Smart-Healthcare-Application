package com.example.doctorservice.dto.request;

import com.example.doctorservice.model.Gender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorUpdateRequest {
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
