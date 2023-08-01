package com.salespage.salespageservice.app.responses.voucherResponse;

import com.salespage.salespageservice.domains.entities.types.DiscountType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVoucherResponse {

  String voucherStoreName;

  String voucherCode;

  Long minPrice;

  Long maxPrice;

  DiscountType discountType;

  VoucherStoreType storeType;

  Long dayToExpireTime;
}
