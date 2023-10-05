package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.productDtos.CreateProductInfoDto;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDetailResponse;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.entities.types.SizeType;
import com.salespage.salespageservice.domains.entities.types.WeightType;
import com.salespage.salespageservice.domains.info.ProductInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("product")
@Data
public class Product extends BaseEntity {
  @Id
  private ObjectId id;

  @Field("product_name")
  private String productName;

  @Field("description")
  private String description;

  @Field("product_info")
  private List<ProductInfo> productInfos = new ArrayList<>();

  @Field("image_urls")
  private List<String> imageUrls = new ArrayList<>();

  @Field("default_image_url")
  private String defaultImageUrl;

  @Field("category_id")
  private String categoryId;

  @Field("rate")
  private Rate rate = new Rate();

  @Field("seller_username")
  private String sellerUsername;

  @Field("seller_store_ids")
  private List<String> sellerStoreIds;

  @Field("is_hot")
  private Boolean isHot;


  public void updateProduct(CreateProductInfoDto dto) {
    productName = dto.getProductName();
    description = dto.getDescription();
    categoryId = dto.getCategoryId();
    sellerStoreIds = dto.getStoreIds();

  }
  public ProductDetailResponse assignToProductDetailResponse() {
    ProductDetailResponse response = new ProductDetailResponse();
    response.assignFromProduct(this);
    return response;
  }

  public ProductDataResponse assignToProductDataResponse() {
    ProductDataResponse response = new ProductDataResponse();
    response.assignFromProduct(this);
    return response;
  }

}
