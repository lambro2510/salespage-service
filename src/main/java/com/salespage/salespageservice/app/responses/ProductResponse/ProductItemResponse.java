package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductItemResponse extends ProductResponse {

  @Schema(description = "ID cửa hàng")
  String storeId;

  @Schema(description = "ID danh mục sản phẩm")
  String categoryId;

  @Override
  public void assignFromProduct(Product product) {
    super.assignFromProduct(product);
    storeId = product.getSellerStoreId();
    categoryId = product.getCategoryId();
  }
}
