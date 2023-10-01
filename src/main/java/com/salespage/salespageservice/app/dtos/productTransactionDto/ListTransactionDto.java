package com.salespage.salespageservice.app.dtos.productTransactionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ListTransactionDto {
  
  String transactionId;

  @Schema(description = "Ghi chú", example = "Vui lòng gửi sản phẩm sớm nhất có thể")
  private String note;

  @Schema(description = "Số lượng", example = "Số lượng")
  private Long quantity;

  @NotBlank(message = "Địa chỉ nhận hàng không được để trống")
  @Schema(description = "Địa chỉ nhận hàng", example = "24/5 Phường Trường Thi")
  private String address;

  @Schema(description = "Mã voucher")
  private String voucherCode;
}
