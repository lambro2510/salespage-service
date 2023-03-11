package com.salespage.salespageservice.domains.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
public class LimitVoucher {
  // TODO Cần tạo composeindex
  @Field("voucher_store_id")
  private ObjectId voucherStoreId;

  @Field("user_id")
  private ObjectId userId;

  @Field("number_voucher_receive")
  private Long numberVoucherReceive;
}
