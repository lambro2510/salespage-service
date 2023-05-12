package com.salespage.salespageservice.app.responses.ProductResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDetailResponse extends ProductResponse {
  List<String> imageUrls = new ArrayList<>();

  String storeName;

  String description;
  List<ProductResponse> similarProducts = new ArrayList<>();

  @JsonProperty("isLike")
  Boolean isLike;

  Float rate;

  String storeImageUrl;

  Rate storeRate;

  @Override
  public void assignFromProduct(Product product) {
    super.assignFromProduct(product);
    imageUrls = product.getImageUrls();
    description = product.getDescription();
  }
}
