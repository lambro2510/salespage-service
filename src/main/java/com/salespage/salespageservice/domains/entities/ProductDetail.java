package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDetailInfoResponse;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("product_detail")
@Data
public class ProductDetail extends BaseEntity{

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  ObjectId id;

  @Field("product_id")
  String productId;

  @Field("type")
  ProductDetailType type;

  @Field("quantity")
  Integer quantity;

  @Field("origin_price")
  Double originPrice;

  @Field("sell_price")
  Double sellPrice;

  @Field("discount_percent")
  Double discountPercent;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductDetailType {

    @Field("type")
    String type;

    @Field("color")
    String color = "#FFFFFF";
  }

  public ProductDetailInfoResponse partnerToResponse(){
    ProductDetailInfoResponse response = new ProductDetailInfoResponse();
    response.setId(id.toHexString());
    response.setProductId(productId);
    response.setQuantity(quantity);
    response.setType(type);
    response.setOriginPrice(originPrice);
    response.setSellPrice(sellPrice);
    response.setDiscountPercent(discountPercent);
    return response;
  }
}
