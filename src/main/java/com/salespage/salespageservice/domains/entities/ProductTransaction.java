package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.infor.ComboInfo;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("product_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTransaction extends BaseEntity {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  @Field("buyer_username")
  @Indexed(name = "buyer_username_idx")
  private String buyerUsername;

  @Field("total_price")
  private Double totalPrice;

  @Field("balance")
  private Double balance;

  @Field("product_transaction_details")
  private List<ProductTransactionDetail> productTransactionDetails;

  @Field("note")
  private String note;

  @Field("combo_info")
  private ComboInfo comboInfo;

}
