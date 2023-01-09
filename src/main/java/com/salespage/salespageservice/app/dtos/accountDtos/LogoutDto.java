package com.salespage.salespageservice.app.dtos.accountDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LogoutDto {

  @NotNull
  private String refreshToken;

}
