package com.salespage.salespageservice.app.dtos.productTransactionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductTransactionDto {

  @NotBlank(message = "ID chi tiết sản phẩm là bắt buộc")
  @Schema(description = "ID chi tiết sản phẩm", required = true)
  private String productDetailId;

  @NotBlank(message = "ID cửa hàng m")
  @Schema(description = "ID cửa hàng", required = true)
  private String storeId;

  @Schema(description = "Mã voucher")
  private String voucherCodeId;

  @Schema(description = "Ghi chú")
  private String note;

  @Schema(description = "Địa chỉ nhận hàng")
  private String address;
}
