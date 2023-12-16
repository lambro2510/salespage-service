package com.salespage.salespageservice.app.responses.ProductComboResponse;

import com.salespage.salespageservice.domains.entities.types.ActiveState;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.Data;

@Data
public class ProductComboResponse {

  String id;

  String comboName;

  DiscountType type;

  ActiveState state;

  Double value;

  Long quantityToUse;

  Double maxDiscount;
}
