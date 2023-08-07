package com.salespage.salespageservice.app.dtos.productDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateProductCategoryTypeDto extends CreateProductCategoryTypeDto {
  @NotBlank(message = "ID danh mục là bắt buộc")
  @Schema(description = "ID danh mục", required = true)
  private String id;
}
