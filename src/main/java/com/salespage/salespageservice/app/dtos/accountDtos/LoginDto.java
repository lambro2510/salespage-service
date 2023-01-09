package com.salespage.salespageservice.app.dtos.accountDtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
    @NotNull
//  @UsernameConstraint
    @Size(min = 5, max = 18)
    private String username;

    @NotNull
    @Size(min = 6, max = 24)
    private String password;
}
