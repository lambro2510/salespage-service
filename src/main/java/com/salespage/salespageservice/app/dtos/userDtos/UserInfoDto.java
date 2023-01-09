package com.salespage.salespageservice.app.dtos.userDtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserInfoDto {
  @NotNull
  private String displayName;

  @NotNull
  private String phoneNumber;

  @NotNull
  private String imageUrl;


}
