package com.salespage.salespageservice.app.dtos.voucherDtos;

import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class VoucherStoreDto {
  @NotNull
  private String voucherStoreName;

  @NotNull
  private VoucherStoreType voucherStoreType;

  @NotNull
  private String productId;

  private Long value;

  @Min(0)
  @Max(100)
  private Long valuePercent;

  private Long maxAblePrice; //Giá trị sản phẩm tối đa có thể áp dụng voucher
  private Long minAblePrice; //Giá trị sản phẩm tối thiểu có thể áp dụng voucher

  private Long maxVoucherPerUser; //Số voucher mà 1 user có thể nhận
}
