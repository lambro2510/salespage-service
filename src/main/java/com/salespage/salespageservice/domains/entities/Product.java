package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.productDtos.CreateProductInfoDto;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDetailResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductItemResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductResponse;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.entities.types.SizeType;
import com.salespage.salespageservice.domains.entities.types.WeightType;
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

  @Field("category_id")
  private String categoryId;

  @Field(value = "price")
  private Double price;

  @Field(value = "sell_price")
  private Double sellPrice;

  @Field(value = "discount_percent")
  private Double discountPercent;

  @Field("rate")
  private Rate rate = new Rate();

  @Field("seller_username")
  private String sellerUsername;

  @Field("seller_store_ids")
  private List<String> sellerStoreIds;

  @Field("detail")
  private ProductDetail detail;

  public void updateProduct(CreateProductInfoDto dto) {
    productName = dto.getProductName();
    description = dto.getDescription();
    categoryId = dto.getCategoryId();
    price = dto.getProductPrice();
    sellerStoreIds = dto.getStoreIds();
    discountPercent = dto.getDiscountPercent();
    sellPrice = price * (discountPercent / 100D);
    ProductDetail productDetail = new ProductDetail();
    productDetail.origin = dto.getOrigin();
    productDetail.isForeign = dto.getIsForeign();
    productDetail.size = dto.getSize();
    productDetail.sizeType = dto.getSizeType();
    productDetail.weight = dto.getWeight();
    productDetail.weightType = dto.getWeightType();
    productDetail.colors = dto.getColors();
    productDetail.isGuarantee = dto.getIsGuarantee();
    productDetail.quantity = dto.getQuantity();
    detail = productDetail;

  }


  public ProductResponse assignToProductResponse() {
    ProductResponse response = new ProductResponse();
    response.assignFromProduct(this);
    return response;
  }


  public ProductDetailResponse assignToProductDetailResponse() {
    ProductDetailResponse response = new ProductDetailResponse();
    response.assignFromProduct(this);
    return response;
  }

  @Data
  public static class ProductDetail {

    @Field("origin")
    String origin;

    @Field("is_foreign")
    Boolean isForeign;

    @Field("size")
    Long size;

    @Field("sizeType")
    SizeType sizeType;

    @Field("weight")
    Long weight;

    @Field("weightType")
    WeightType weightType;

    @Field("colors")
    List<String> colors;

    @Field("is_guarantee")
    Boolean isGuarantee;

    @Field("quantity")
    Long quantity = 0L;
  }
}
