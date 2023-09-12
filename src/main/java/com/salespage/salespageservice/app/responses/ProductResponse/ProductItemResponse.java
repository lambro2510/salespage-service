package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.app.responses.storeResponse.SellerStoreResponse;
import com.salespage.salespageservice.domains.entities.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductItemResponse extends ProductResponse {

  @Schema(description = "ID cửa hàng")
  List<SellerStoreResponse> stores;

  @Schema(description = "ID danh mục sản phẩm")
  String categoryId;

  @Override
  public void assignFromProduct(Product product) {
    super.assignFromProduct(product);
    categoryId = product.getCategoryId();
  }
}
