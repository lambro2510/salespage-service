package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.ProductDetail;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class ProductDetailDto {
  String productId;

  ProductDetail.ProductDetailType type;

  Integer quantity;

  Double originPrice;

  @Min(0)
  @Max(100)
  Double discountPercent;
}
