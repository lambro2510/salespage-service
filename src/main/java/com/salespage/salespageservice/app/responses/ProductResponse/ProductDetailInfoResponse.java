package com.salespage.salespageservice.app.responses.ProductResponse;

import lombok.Data;

@Data
public class ProductDetailInfoResponse {
  String productDetailId;

  String productId;

  String type;

  String color;

  Integer quantity;

  Double originPrice;

  Double sellPrice;

  Double discountPercent;
}
