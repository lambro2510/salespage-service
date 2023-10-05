package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.ProductDetail;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class ProductDetailDto {
  String productId;

  ProductDetail.ProductDetailType type;

  Integer quantity;

  Double originPrice;

  Double sellPrice;

  Double discountPercent;
}
