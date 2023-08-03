package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.entities.types.CategoryType;
import com.salespage.salespageservice.domains.entities.types.TimeType;
import lombok.Data;

@Data
public class ProductCategoryResponse {

  private String categoryId;

  private String categoryName;

  private CategoryType categoryType;

  private String description;

  private TimeType timeType;

  private Integer timeValue;

  private String productType;

  public void partnerFromCategory(ProductCategory productCategory) {
    categoryId = productCategory.getId().toHexString();
    categoryName = productCategory.getCategoryName();
    categoryType = productCategory.getCategoryType();
    description = productCategory.getDescription();
    timeType = productCategory.getTimeType();
    timeValue = productCategory.getTimeValue();
    productType = productCategory.getProductType();
  }
}
