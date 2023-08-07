package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductTypeDto {

  @NotBlank(message = "Loại sản phẩm là bắt buộc")
  @Schema(description = "Loại sản phẩm", required = true)
  private String productType;

  @NotBlank(message = "Tên loại sản phẩm là bắt buộc")
  @Schema(description = "Tên loại sản phẩm", required = true)
  private String typeName;

  @Schema(description = "Mô tả")
  private String description;

  @NotNull(message = "Trạng thái là bắt buộc")
  @Schema(description = "Trạng thái loại sản phẩm", required = true)
  private ProductTypeStatus status;
}
