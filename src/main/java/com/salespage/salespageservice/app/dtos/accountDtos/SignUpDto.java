package com.salespage.salespageservice.app.dtos.accountDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class SignUpDto {

  @NotBlank(message = "Tên tài khoản là bắt buộc")
  @Size(min = 8, max = 18, message = "Tên tài khoản phải từ 8 đến 18 ký tự")
  @Schema(description = "Tên tài khoản của người dùng")
  private String username;

  @NotBlank(message = "Mật khẩu là bắt buộc")
  @Size(min = 6, max = 24, message = "Mật khẩu phải từ 6 đến 24 ký tự")
  @Schema(description = "Mật khẩu của người dùng")
  private String password;

  @NotBlank(message = "Xác nhận mật khẩu là bắt buộc")
  @Size(min = 6, max = 24, message = "Xác nhận mật khẩu phải từ 6 đến 24 ký tự")
  @Schema(description = "Xác nhận lại mật khẩu của người dùng")
  private String confirmPassword;

  @NotBlank(message = "Số điện thoại là bắt buộc")
  @Pattern(regexp = "^(?:[0-9] ?){6,14}[0-9]$", message = "Số điện thoại phải là số điện thoại hợp lệ")
  @Schema(description = "Số điện thoại của người dùng")
  private String phoneNumber;

  @Schema(description = "Quyền của người dùng", example = "USER")
  private UserRole userRole;

}
