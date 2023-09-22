package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.types.CategoryType;
import com.salespage.salespageservice.domains.entities.types.TimeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateProductCategoryTypeDto {

  @NotBlank(message = "Tên danh mục là bắt buộc")
  @Schema(description = "Tên danh mục", required = true)
  private String categoryName;

  @NotNull(message = "Loại danh mục là bắt buộc")
  @Schema(description = "Loại danh mục", required = true)
  private CategoryType categoryType;

  @Schema(description = "Mô tả")
  private String description;

  @Schema(description = "Độ tuối người dùng")
  private String rangeAge;

  @NotBlank(message = "Loại sản phẩm là bắt buộc")
  @Schema(description = "Loại sản phẩm", required = true)
  private String productType;
}
