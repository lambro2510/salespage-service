package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.types.CategoryType;
import lombok.Data;

@Data
public class ProductCategoryResponse {

  private String id;

  private String categoryName;

  private String description;

  private CategoryType categoryType;

  private String productTypeId;

  private String productType;

  private String productTypeName;

  private String rangeAge;

}
