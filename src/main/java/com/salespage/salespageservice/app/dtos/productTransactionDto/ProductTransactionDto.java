package com.salespage.salespageservice.app.dtos.productTransactionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductTransactionDto extends ProductTransactionInfoDto {

  @NotBlank(message = "ID sản phẩm là bắt buộc")
  @Schema(description = "ID sản phẩm", required = true)
  private String productId;

  @Schema(description = "Mã voucher")
  private String voucherCode;
}
