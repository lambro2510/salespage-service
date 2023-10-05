package com.salespage.salespageservice.app.responses.CartResponse;

import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboDetailResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Data
public class CartByStoreResponse {

  String storeId;

  String storeName;

  List<CartResponse> cartResponses = new ArrayList<>();

  List<ProductComboDetailResponse> combos = new ArrayList<>();

  ProductComboDetailResponse bestCombo;

  public void setBestCombo(){
    Optional<ProductComboDetailResponse> bestCombo = combos.stream()
        .filter(combo -> combo.getTotalPrice() > 0)
        .max(Comparator.comparingDouble(ProductComboDetailResponse::getTotalPrice));
    setBestCombo(bestCombo.get());
  }
}
