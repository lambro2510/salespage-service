package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.Product;
import lombok.Data;

@Data
public class ProductItemResponse extends ProductDataResponse {
  String storeId;

  @Override
  public void assignFromProduct(Product product) {
    super.assignFromProduct(product);
    storeId = product.getSellerStoreId();
  }
}
