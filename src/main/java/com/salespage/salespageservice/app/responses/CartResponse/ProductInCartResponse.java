package com.salespage.salespageservice.app.responses.CartResponse;

import com.salespage.salespageservice.domains.entities.Product;
import lombok.Data;

@Data
public class ProductInCartResponse {
  String productId;

  String productName;

  String imageUrl;


  public ProductInCartResponse(Product product) {
    productId = product.getId().toHexString();
    productName = product.getProductName();
    imageUrl = product.getDefaultImageUrl();

  }
}
