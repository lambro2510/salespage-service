package com.salespage.salespageservice.app.dtos.accountDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
  @NotNull(message = "Tên đăng nhập không được để trống")
  @Size(min = 8, max = 18, message = "Tên đăng nhập phải từ 8 đến 18 ký tự")
  @Schema(description = "Tên đăng nhập tài khoản", example = "lambro25102001")
  private String username;

  @NotNull(message = "Mật khẩu không được để trống")
  @Size(min = 6, max = 24, message = "Mật khẩu phải tử 6 đến 24 ký tự")
  @Schema(description = "Mật khẩu của tài khoản", example = "Banhmy09@")
  private String password;
}
