package com.salespage.salespageservice.app.responses.ProductResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDetailResponse extends ProductResponse {
  List<String> imageUrls = new ArrayList<>();

  String storeName;

  List<ProductResponse> similarProducts = new ArrayList<>();

}
