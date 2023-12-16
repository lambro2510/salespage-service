package com.salespage.salespageservice.domains.entities.infor;

import com.salespage.salespageservice.domains.entities.ProductCombo;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComboInfo {

  Boolean isUseCombo = true;

  String comboId;

  String comboName;

  DiscountType discountType;

  Double value;

  Double totalDiscount;

  Double sellPrice;

  public ComboInfo(ProductCombo combo, Double discount, Double price) {
    if (combo == null) {
      isUseCombo = false;
    } else {
      comboId = combo.getId().toHexString();
      comboName = combo.getComboName();
      discountType = combo.getType();
      value = combo.getValue();
    }
    totalDiscount = discount;
    sellPrice = price;
  }
}
