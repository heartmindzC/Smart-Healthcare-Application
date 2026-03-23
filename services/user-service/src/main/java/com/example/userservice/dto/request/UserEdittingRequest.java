package com.example.userservice.dto.request;

import com.example.userservice.model.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@NotNull
public class UserEdittingRequest {
    @Pattern(regexp = "^\\d{10}$", message = "PHONE_INVALID")
    private String phone;
    @Email(message = "EMAIL_INVALID")
    private String email;
    @NotEmpty(message = "EMPTY_NAME")
    private String fullname;
    @NotEmpty(message = "EMPTY_ADDRESS")
    private String address;
    @NotNull(message = "NULL_DATE")
    private Date birth;
    @NotNull (message = "NULL_GENDER")
    private Gender gender;
}
