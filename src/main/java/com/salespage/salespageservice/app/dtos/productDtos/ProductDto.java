package com.salespage.salespageservice.app.dtos.productDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDto extends ProductInfoDto {

  @NotBlank(message = "Id của sản phẩm không được để trống")
  @Schema(description = "Id của sản phẩm", example = "P0001")
  private String productId;
}
