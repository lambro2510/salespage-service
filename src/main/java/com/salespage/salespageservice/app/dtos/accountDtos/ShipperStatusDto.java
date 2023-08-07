package com.salespage.salespageservice.app.dtos.accountDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ShipperStatusDto {

  @NotNull(message = "Trạng thái là bắt buộc")
  @Schema(description = "Trạng thái của người giao hàng", required = true)
  private Boolean status;

  @NotBlank(message = "Kinh độ là bắt buộc")
  @Schema(description = "Kinh độ của người giao hàng")
  private String longitude;

  @NotBlank(message = "Vĩ độ là bắt buộc")
  @Schema(description = "Vĩ độ của người giao hàng")
  private String latitude;
}
