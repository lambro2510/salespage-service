package com.salespage.salespageservice.app.responses.voucherResponse;

import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoucherStoreResponse {

  @Schema(description = "Id của cửa hàng voucher", example = "VCST123")
  private String voucherStoreId;

  @Schema(description = "Tên của cửa hàng voucher", example = "Voucher Store A")
  private String voucherStoreName;

  @Schema(description = "Tổng số lượng voucher trong cửa hàng", example = "1000")
  private Long totalQuantity;

  @Schema(description = "Tổng số lượng voucher đã sử dụng", example = "500")
  private Long totalUsed;

  @Schema(description = "Trạng thái của cửa hàng voucher", example = "ACTIVE")
  private VoucherStoreStatus voucherStoreStatus;

  @Schema(description = "Tên sản phẩm (cửa hàng)", example = "ABC")
  private String name;

  @Schema(description = "Loại của cửa hàng voucher", example = "STORE")
  private VoucherStoreType voucherStoreType;

  @Schema(description = "Loại của cửa hàng voucher", example = "PERCENT")
  private DiscountType discountType;

  @Schema(description = "ID của sản phẩm được áp dụng voucher", example = "123")
  private String refId;

  @Schema(description = "Giá trị của voucher", example = "10000")
  private Double value;
}
