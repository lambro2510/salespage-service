package com.salespage.salespageservice.domains.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("voucher_code_limit")
@Data
public class VoucherCodeLimit {
  @Id
  private ObjectId id;

  @Field("username")
  private String username;

  @Field("voucher_store_id")
  private String voucherStoreId;

  @Field("number_receive_voucher")
  private Long numberReceiveVoucher;
}
