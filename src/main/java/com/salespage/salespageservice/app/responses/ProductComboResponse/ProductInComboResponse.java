package com.salespage.salespageservice.app.responses.ProductComboResponse;

import com.salespage.salespageservice.domains.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInComboResponse {

  private String productId;

  private String productName;

  private String imageUrl;

  private Boolean isInCombo = false;

  public ProductInComboResponse(Product product){
    productId = product.getId().toHexString();
    productName = product.getProductName();
    imageUrl = product.getDefaultImageUrl();
  }
}
