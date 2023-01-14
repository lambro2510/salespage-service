package com.salespage.salespageservice.app.dtos.accountDtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SignUpDto {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Confirm password is required")
    private String confirmPassword;

    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    private Date dateOfBirth;
}
