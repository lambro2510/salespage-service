package com.salespage.salespageservice.app.dtos.accountDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SignUpDto {

    @NotNull(message = "Username is required")
    @Schema(description = "The username of the account", example = "john_doe")
    private String username;

    @NotNull(message = "Password is required")
    @Schema(description = "The password of the account", example = "my_secret_password")
    private String password;

    @NotNull(message = "Confirm password is required")
    @Schema(description = "The confirmation of the password", example = "my_secret_password")
    private String confirmPassword;

    @NotNull(message = "First name is required")
    @Schema(description = "The first name of the account holder", example = "John")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Schema(description = "The last name of the account holder", example = "Doe")
    private String lastName;

    @NotNull(message = "Email is required")
    @Schema(description = "The email address of the account holder", example = "john.doe@example.com")
    private String email;

    @NotNull(message = "Phone number is required")
    @Schema(description = "The phone number of the account holder", example = "+1-202-555-0191")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Schema(description = "The date of birth of the account holder", example = "1980-01-01")
    private Date dateOfBirth;
}
