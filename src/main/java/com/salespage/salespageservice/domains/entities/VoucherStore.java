package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.app.dtos.voucherDtos.UpdateVoucherStoreDto;
import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("voucher_store")
@Data
public class VoucherStore extends BaseEntity{
  @Id
  private ObjectId id;

  @Field("voucher_store_name")
  private String voucherStoreName;

  @Field("voucher_store_type")
  private VoucherStoreType voucherStoreType;

  @Field("product_id")
  private String productId; //Sản phẩm áp dụng cho voucher, nếu type là product thì cho phép nhưới dùng mua sản
                              //phẩm miễn phí, nếu type = sale thì giảm giá sản phẩm đó

  @Field("value")
  private Long value;

  @Field("voucher_store_detail")
  private VoucherStoreDetail voucherStoreDetail;

  @Field("voucher_store_status")
  private VoucherStoreStatus voucherStoreStatus = VoucherStoreStatus.INACTIVE;

  @Field("created_by")
  private String createdBy;

  @Data
  public static class VoucherStoreDetail{

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

  public void updatedVoucherStore(UpdateVoucherStoreDto updateVoucherStoreDto){
    setVoucherStoreName(updateVoucherStoreDto.getVoucherStoreName());
    setVoucherStoreType(updateVoucherStoreDto.getVoucherStoreType());

    if(voucherStoreType == VoucherStoreType.DISCOUNT_PERCENT){
      setValue(updateVoucherStoreDto.getValuePercent());
    }
    else if(voucherStoreType == VoucherStoreType.MONEY || voucherStoreType == VoucherStoreType.DISCOUNT){
      setValue(updateVoucherStoreDto.getValue());
    }

    VoucherStore.VoucherStoreDetail voucherStoreDetail = new VoucherStore.VoucherStoreDetail();

    voucherStoreDetail.setMaxVoucherPerUser(updateVoucherStoreDto.getMaxVoucherPerUser());
    voucherStoreDetail.setMaxAblePrice(updateVoucherStoreDto.getMaxAblePrice());
    voucherStoreDetail.setMinAblePrice(updateVoucherStoreDto.getMinAblePrice());
    setVoucherStoreDetail(voucherStoreDetail);
  }
}
