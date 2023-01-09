package com.salespage.salespageservice.app.dtos.accountDtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SignUpDto {

  @NotNull
  private String username;

  @NotNull
  private String password;

  @NotNull
  private String firstName;

  @NotNull
  private String lastName;

  @NotNull
  private String email;

  @NotNull
  private String phoneNumber;

  @NotNull
  private Date dateOfBirth;

}
