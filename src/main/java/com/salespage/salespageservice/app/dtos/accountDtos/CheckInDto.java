package com.salespage.salespageservice.app.dtos.accountDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInDto {

  @NotBlank(message = "Kinh độ là bắt buộc")
  @Schema(description = "Kinh độ của người giao hàng")
  private String longitude;

  @NotBlank(message = "Vĩ độ là bắt buộc")
  @Schema(description = "Vĩ độ của người giao hàng")
  private String latitude;

  @Schema(description = "Tên người dùng")
  private String username;
}
