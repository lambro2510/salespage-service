package com.salespage.salespageservice.app.responses.voucherResponse;

import com.salespage.salespageservice.domains.entities.types.DiscountType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVoucherResponse {

  @Schema(description = "Tên cửa hàng voucher")
  String voucherStoreName;

  @Schema(description = "Mã voucher")
  String voucherCode;

  @Schema(description = "Giá trị đơn hàng tối thiểu")
  Long minPrice;

  @Schema(description = "Giá trị đơn hàng tối đa")
  Long maxPrice;

  @Schema(description = "Loại giảm giá")
  DiscountType discountType;

  @Schema(description = "Loại cửa hàng voucher")
  VoucherStoreType storeType;

  @Schema(description = "Số ngày còn lại cho đến khi voucher hết hạn")
  Long dayToExpireTime;
}
