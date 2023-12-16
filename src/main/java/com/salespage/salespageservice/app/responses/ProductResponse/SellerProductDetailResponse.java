package com.salespage.salespageservice.app.responses.ProductResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.app.responses.storeResponse.SellerStoreResponse;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.info.ProductInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SellerProductDetailResponse {
  String id;

  String productName;

  String defaultImageUrl;

  Rate rate;

  @JsonProperty("isHot")
  Boolean isHot;

  List<ProductInfo> productInfos = new ArrayList<>();

  String categoryId;

  ProductCategoryResponse productCategory;

  List<String> sellerStoreIds = new ArrayList<>();

  List<SellerStoreResponse> stores = new ArrayList<>();

  List<TypeDetailResponse> typeDetails = new ArrayList<>();


}
