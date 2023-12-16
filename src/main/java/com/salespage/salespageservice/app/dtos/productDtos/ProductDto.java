package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.info.ProductInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDto {

  private String productName;

  private String description;

  private String categoryId;

  private List<String> sellerStoreIds;

  private List<ProductInfo> productInfos = new ArrayList<>();

}
