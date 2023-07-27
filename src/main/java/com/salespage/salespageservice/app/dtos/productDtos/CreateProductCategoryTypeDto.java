package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.types.CategoryType;
import com.salespage.salespageservice.domains.entities.types.TimeType;
import lombok.Data;

@Data
public class CreateProductCategoryTypeDto {
  private String categoryName;

  private CategoryType categoryType;

  private String description;

  private TimeType timeType;

  private Integer timeValue;

  private String productType;
}
