package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.productDtos.CreateProductInfoDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductInfoDto;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductResponse;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("product")
@Data
public class Product extends BaseEntity {
  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;

  @Field("product_name")
  private String productName;

  @Field("description")
  private String description;

  @Field("image_urls")
  private List<String> imageUrls = new ArrayList<>();

  @Field("default_image_url")
  private String defaultImageUrl;

  @Field("product_type")
  private String type;

  @Field("price")
  private BigDecimal price;

  @Field("rate")
  private Rate rate = new Rate();

  @Field("selling_address")
  private String sellingAddress;

  @Field("seller_username")
  private String sellerUsername;

  @Field("seller_store_id")
  private String sellerStoreId;

  public void updateProduct(CreateProductInfoDto dto) {
    productName = dto.getProductName();
    description = dto.getDescription();
    type = dto.getType();
    price = BigDecimal.valueOf(dto.getPrice());
    sellingAddress = dto.getSellingAddress();
    sellerStoreId = dto.getStoreId();
  }

  public void updateProductInfo(ProductInfoDto dto) {
    updateProduct(dto);
    defaultImageUrl = dto.getDefaultImage();
  }

  public ProductDataResponse assignToProductDataResponse() {
    ProductDataResponse response = new ProductDataResponse();
    response.assignFromProduct(this);
    return response;
  }

  public ProductResponse assignToProductResponse() {
    ProductResponse response = new ProductResponse();
    response.assignFromProduct(this);
    return response;
  }

}
