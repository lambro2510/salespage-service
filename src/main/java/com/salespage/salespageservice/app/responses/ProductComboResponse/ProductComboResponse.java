package com.salespage.salespageservice.app.responses.ProductComboResponse;

import com.salespage.salespageservice.domains.entities.types.ActiveState;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductComboResponse {

  private String id;

  private String comboName;

  private DiscountType type;

  private ActiveState state;

  private Double value;

  private Long quantityToUse;

  private Double maxDiscount;
}
