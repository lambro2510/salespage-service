package com.salespage.salespageservice.app.dtos.productDtos;

import lombok.Data;

@Data
public class ProductDetailDto {
  String productId;

  String type;

  String color;

  Integer quantity;

  Double originPrice;

  Double discountPercent;
}
