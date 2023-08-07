package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.status.ProductTypeDetailStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateTypeDetailStatusDto {

  @NotBlank(message = "ID chi tiết loại sản phẩm là bắt buộc")
  @Schema(description = "ID chi tiết loại sản phẩm", required = true)
  private String id;

  @NotNull(message = "Trạng thái chi tiết loại sản phẩm là bắt buộc")
  @Schema(description = "Trạng thái chi tiết loại sản phẩm", required = true)
  private ProductTypeDetailStatus status;
}
