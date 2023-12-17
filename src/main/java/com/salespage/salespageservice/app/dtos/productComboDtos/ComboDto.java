package com.salespage.salespageservice.app.dtos.productComboDtos;

import com.salespage.salespageservice.domains.entities.types.ActiveState;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.Data;

@Data
public class ComboDto {

  private String comboName;

  private DiscountType type;

  private ActiveState state;

  private Double value;

  private Long quantityToUse;

  private Double maxDiscount;

  private String storeId;
}
