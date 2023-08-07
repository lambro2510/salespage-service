package com.salespage.salespageservice.app.dtos.productDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductTypeDetailDto {

  @NotBlank(message = "ID sản phẩm là bắt buộc")
  @Schema(description = "ID sản phẩm", required = true)
  private String productId;

  @NotBlank(message = "Tên loại sản phẩm là bắt buộc")
  @Schema(description = "Tên loại sản phẩm", required = true)
  private String typeName;

  @NotBlank(message = "Tên chi tiết loại sản phẩm là bắt buộc")
  @Schema(description = "Tên chi tiết loại sản phẩm", required = true)
  private String typeDetailName;

  @Schema(description = "Ghi chú")
  private String note;
}
