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
import java.util.Objects;

@Document("product_transaction_detail")
@Getter
@Setter
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

  @Field("transaction_id")
  private String transactionId;

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

  @Field("username")
  private String username;

  @Field("message")
  private List<Message> messages = new ArrayList<>();

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Message extends BaseEntity {
    private String sender;

    private String receiver;

    private String content;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ProductTransactionDetail)) return false;
    if (!super.equals(o)) return false;
    ProductTransactionDetail that = (ProductTransactionDetail) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getId());
  }
}
