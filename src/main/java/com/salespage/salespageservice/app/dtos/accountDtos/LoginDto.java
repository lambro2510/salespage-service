package com.salespage.salespageservice.app.dtos.accountDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
  @Schema(description = "Tên đăng nhập tài khoản", example = "lambro25102001")
  private String username;

  @NotNull(message = "Tên đăng nhập không được để trống")
  private String mobile;

  @NotNull(message = "Tên đăng nhập không được để trống")
  private String otp;

  @Schema(description = "Mật khẩu của tài khoản", example = "Banhmy09@")
  private String password;
}
