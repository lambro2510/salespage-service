package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.app.dtos.voucherDtos.UpdateVoucherStoreDto;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Document("voucher_store")
@Data
public class VoucherStore extends BaseEntity {
  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  @Field("voucher_store_name")
  private String voucherStoreName;

  @Field("voucher_store_type")
  private VoucherStoreType voucherStoreType;

  @Field("ref_id")
  private String refId;

  @Field("discount_type")
  private DiscountType discountType;

  @Field("value")
  private Double value;

  @Field("voucher_store_detail")
  private VoucherStoreDetail voucherStoreDetail;

  @Field("voucher_store_status")
  private VoucherStoreStatus voucherStoreStatus = VoucherStoreStatus.INACTIVE;

  @Field("created_by")
  private String createdBy;

  public void updatedVoucherStore(UpdateVoucherStoreDto updateVoucherStoreDto) {
    setVoucherStoreName(updateVoucherStoreDto.getVoucherStoreName());
    setVoucherStoreType(updateVoucherStoreDto.getVoucherStoreType());
    setVoucherStoreStatus(updateVoucherStoreDto.getVoucherStoreStatus());
    if (updateVoucherStoreDto.getDiscountType() == DiscountType.PERCENT) {
      if (updateVoucherStoreDto.getValuePercent() <= 0 || updateVoucherStoreDto.getValuePercent() >= 100)
        throw new BadRequestException("Giá trị giảm giá không hợp lệ");
      setValue(updateVoucherStoreDto.getValuePercent());
    } else {
      setValue(updateVoucherStoreDto.getValue());
    }
    setDiscountType(updateVoucherStoreDto.getDiscountType());
    VoucherStoreDetail voucherStoreDetail = new VoucherStoreDetail();
    voucherStoreDetail.setMaxVoucherPerUser(updateVoucherStoreDto.getMaxVoucherPerUser());
    voucherStoreDetail.setMaxAblePrice(updateVoucherStoreDto.getMaxAblePrice());
    voucherStoreDetail.setMinAblePrice(updateVoucherStoreDto.getMinAblePrice());
    setVoucherStoreDetail(voucherStoreDetail);
  }

  @Data
  public static class VoucherStoreDetail {

    @Field("quantity")
    private Long quantity = 0L;

    @Field("quantity_used")
    private Long quantityUsed = 0L;

    @Field("max_able_price")
    private Long maxAblePrice; //Giá trị sản phẩm tối đa voucher dạng sale có thể sử dụng

    @Field("min_able_price")
    private Long minAblePrice; //Giá trị sản phẩm tối thiểu voucher dạng sale có thể sử dụng

    @Field("max_voucher_per_user")
    private Long maxVoucherPerUser;
  }
}
