package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("product_transaction_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTransactionDetail extends BaseEntity{

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  @Field("product_detail_id")
  @Indexed(name = "product_detail_id_idx")
  private String productDetailId;

  @Field("product_detail")
  private ProductDetail productDetail;

  @Field("store_id")
  private String storeId;

  @Field("store")
  private SellerStore store;

  @Field("state")
  private ProductTransactionState state;

  @Field("quantity")
  private Long quantity = 0L;

  @Field("total_price")
  private Double totalPrice = 0D;

  @Field("note")
  private String note;

  @Field("voucher_info")
  private VoucherInfo voucherInfo;

  @Field("ship_cod")
  private Double shipCod;

  @Field("address")
  private String address;
  
  @Field("message")
  private List<Message> messages = new ArrayList<>();

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Message extends BaseEntity {
    private String sender;

    private String receiver;

    private String content;

  }
}
