package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.app.responses.voucherResponse.VoucherCodeResponse;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import com.salespage.salespageservice.domains.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Document("voucher_code")
@Data
@CompoundIndex(name = "code_owner_status_index", def = "{'code' : 1, 'ownerId' : 1, 'voucherCodeStatus' : 1}", unique = true)
public class VoucherCode extends BaseEntity {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  @Field("voucher_store_id")
  private String voucherStoreId;

  @Field("owner_id")
  private String username;

  @Field("used_at")
  private Long userAt;

  @Field("code")
  private String code;

  @Field("expire_time")
  private Long expireTime;

  @Field("voucher_code_status")
  private VoucherCodeStatus voucherCodeStatus = VoucherCodeStatus.NEW;

  public VoucherCodeResponse convertTovoucherCodeResponse() {
    VoucherCodeResponse response = new VoucherCodeResponse();
    response.setVoucherCode(code);
    response.setUsedAt(userAt);
    response.setUsedBy(username);
    response.setVoucherCodeStatus(voucherCodeStatus);
    response.setExpireTime(expireTime);
    return response;
  }

  public boolean checkVoucher(String username) {
    return expireTime <= DateUtils.nowInMillis() || !voucherCodeStatus.equals(VoucherCodeStatus.OWNER) || !Objects.equals(username, this.getUsername());
  }
}
