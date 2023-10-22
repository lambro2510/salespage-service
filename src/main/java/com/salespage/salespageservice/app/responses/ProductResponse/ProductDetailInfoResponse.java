package com.salespage.salespageservice.app.responses.ProductResponse;

import com.salespage.salespageservice.domains.entities.ProductDetail;
import lombok.Data;

@Data
public class ProductDetailInfoResponse {
  String id;

  String productId;

  ProductDetail.ProductDetailType type;

  Integer quantity;

  Double originPrice;

  Double sellPrice;

  Double discountPercent;
}
