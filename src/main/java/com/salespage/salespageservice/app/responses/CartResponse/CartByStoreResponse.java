package com.salespage.salespageservice.app.responses.CartResponse;

import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboDetailResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartByStoreResponse {

  String storeId;

  String storeName;

  List<CartResponse> cartResponses = new ArrayList<>();

  List<ProductComboDetailResponse> combos = new ArrayList<>();

  ProductComboDetailResponse bestCombo;

  public void setBestCombo() {
    if (!combos.isEmpty()) {
      Double maxDiscount = 0D;
      for (ProductComboDetailResponse combo : combos) {
        if (combo.getMaxDiscount() > maxDiscount && Boolean.TRUE.equals(combo.getCanUse())) {
          bestCombo = combo;
        }
      }
    }
  }

}
