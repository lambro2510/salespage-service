package com.salespage.salespageservice.app.responses.ProductComboResponse;

import com.salespage.salespageservice.app.responses.ProductResponse.ProductInfoResponse;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductComboDetailResponse {

  String id;

  Boolean canUse = true;

  Double totalPrice = 0D;

  String comboName;

  DiscountType type;

  List<ProductInfoResponse> products = new ArrayList<>();

  Double value;

  Long quantityToUse;

  Double maxDiscount;
}
