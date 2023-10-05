package com.salespage.salespageservice.app.responses.ProductComboResponse;

import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.Data;

@Data
public class ProductComboDetailResponse {

  String id;

  Boolean canUse = true;

  Double totalPrice = 0D;

  String comboName;

  DiscountType type;

  Double value;

  Long quantityToUse;

  Double maxDiscount;
}
