package com.salespage.salespageservice.app.dtos.accountDtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
    @NotNull(message = "Username is required")
    @Size(min = 8, max = 18, message = "Username must be between 8 and 18 characters")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 6, max = 24, message = "Password must be between 6 and 24 characters")
    private String password;
}
