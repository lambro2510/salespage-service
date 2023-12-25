package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart extends BaseEntity {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  ObjectId id;

  @Field("product_detail_id")
  String productDetailId;

  @Field("product")
  Product product ;

  @Field("product_name")
  String productName;

  @Field("username")
  String username;

  @Field("voucher_code_id")
  String voucherCodeId;

  @Field("store_id")
  String storeId;

  @Field("voucher_info")
  VoucherInfo voucherInfo;

  @Field("quantity")
  Long quantity;

  public void addQuantity(long amount) {
    quantity += amount;
  }
}
