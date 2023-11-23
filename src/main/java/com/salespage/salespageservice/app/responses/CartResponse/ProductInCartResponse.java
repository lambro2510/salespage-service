package com.salespage.salespageservice.app.responses.CartResponse;

import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductCombo;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.Data;

@Data
public class ProductInCartResponse {
  String productId;

  String productName;

  String imageUrl;


  public ProductInCartResponse(Product product){
    productId = product.getId().toHexString();
    productName = product.getProductName();
    imageUrl = product.getDefaultImageUrl();

  }
}
