package com.salespage.salespageservice.app.responses.ProductResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.domains.entities.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDetailResponse extends ProductResponse {
  List<String> imageUrls = new ArrayList<>();

  String storeName;

  List<ProductResponse> similarProducts = new ArrayList<>();

  @JsonProperty("isLike")
  Boolean isLike;

  Float rate;

  @Override
  public void assignFromProduct(Product product) {
    super.assignFromProduct(product);
    imageUrls = product.getImageUrls();
  }
}
