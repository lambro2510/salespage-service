package com.salespage.salespageservice.app.responses.Cart;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartByStoreResponse {

  String storeId;

  String storeName;

  List<CartResponse> cartResponses = new ArrayList<>();
}