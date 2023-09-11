package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.entities.types.CategoryType;
import com.salespage.salespageservice.domains.entities.types.TimeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductCategoryResponse {

  @Schema(description = "ID danh mục sản phẩm")
  private String categoryId;

  @Schema(description = "Tên danh mục sản phẩm")
  private String categoryName;

  @Schema(description = "Loại danh mục sản phẩm")
  private CategoryType categoryType;

  @Schema(description = "Mô tả danh mục sản phẩm")
  private String description;

  @Schema(description = "Loại thời gian")
  private TimeType timeType;

  @Schema(description = "Giá trị thời gian")
  private Integer timeValue;

  @Schema(description = "Loại sản phẩm")
  private String productType;

  public void partnerFromCategory(ProductCategory productCategory) {
    categoryId = productCategory.getId().toHexString();
    categoryName = productCategory.getCategoryName();
    categoryType = productCategory.getCategoryType();
    description = productCategory.getDescription();
    productType = productCategory.getProductType();
  }
}
