package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDataResponse {
  private String productId;

  private String productName;

  private BigDecimal productPrice;

  private float totalRate;

  private float avgPoint;

  private String sellerUsername;

  private String storeName;

  public void assignFromProduct(Product product) {
    productId = product.getId().toHexString();
    productName = product.getProductName();
    productPrice = product.getPrice();
    sellerUsername = product.getSellerUsername();
    totalRate = product.getRate().getTotalRate();
    avgPoint = product.getRate().getAvgPoint();
  }
}
