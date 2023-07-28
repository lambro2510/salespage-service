package com.salespage.salespageservice.app.dtos.productDtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateProductCategoryTypeDto extends CreateProductCategoryTypeDto{
  @NotNull
  private String id;
}
