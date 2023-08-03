package com.salespage.salespageservice.app.dtos.userDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserInfoDto {
  @NotBlank(message = "Tên hiển thị là bắt buộc")
  @Size(min = 2, max = 50, message = "Tên hiển thị phải có độ dài từ 2 đến 50 ký tự")
  @Schema(description = "Tên hiển thị", example = "Nguyễn Văn A")
  private String displayName;

  @NotBlank(message = "Số điện thoại là bắt buộc")
  @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Số điện thoại phải là số điện thoại quốc tế hợp lệ")
  @Schema(description = "Số điện thoại", example = "+84123456789")
  private String phoneNumber;

  @NotBlank(message = "Link ảnh là bắt buộc")
  @Schema(description = "Link ảnh", example = "https://example.com/image.jpg")
  private String imageUrl;

}
