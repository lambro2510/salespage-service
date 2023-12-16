package com.salespage.salespageservice.app.responses.CartResponse;

import com.salespage.salespageservice.domains.entities.ProductCombo;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListProductCartResponse {
  String comboId;

  String comboName;

  DiscountType discountType;

  Double value;

  List<ProductInCartResponse> products = new ArrayList<>();

  public ListProductCartResponse(ProductCombo combo) {
    comboId = combo.getId().toHexString();
    comboName = combo.getComboName();
    discountType = combo.getType();
    value = combo.getValue();
  }
}
