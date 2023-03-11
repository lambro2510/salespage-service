package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.types.VoucherStatus;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("voucher")
public class VoucherCode {
  @Id
  private ObjectId id;

  @Field("voucher_store_id")
  private ObjectId voucherStoreId;

  @Field("voucher_status")
  private VoucherStatus voucherStatus;

  @Field("voucher_code")
  private String voucherCode;

  @Field("used_at")
  private LocalDateTime usedAt;

  @Field("expire_time")
  private LocalDateTime expireTime;

  @Field("user_id")
  private ObjectId userId;

}
