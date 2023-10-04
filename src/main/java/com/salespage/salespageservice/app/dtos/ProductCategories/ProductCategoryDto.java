package com.salespage.salespageservice.app.dtos.ProductCategories;

import com.salespage.salespageservice.domains.entities.types.CategoryType;
import lombok.Data;

@Data
public class ProductCategoryDto {

  private String categoryName;

  private String description;

  private CategoryType categoryType;

  private String productTypeId;

  private String rangeAge;

}
