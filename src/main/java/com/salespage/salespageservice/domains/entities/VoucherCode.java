package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("voucher_code")
@Data
public class VoucherCode extends BaseEntity{

  @Id
  private ObjectId id;

  @Field("voucher_store_id")
  private String voucherStoreId;

  @Field("owner_id")
  private String ownerId;

  @Field("code")
  private String code;

  @Field("expire_time")
  private Date expireTime;

  @Field("voucher_code_status")
  private VoucherCodeStatus voucherCodeStatus = VoucherCodeStatus.NEW;

}
