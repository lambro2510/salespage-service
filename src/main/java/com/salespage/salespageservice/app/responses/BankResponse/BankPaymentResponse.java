package com.salespage.salespageservice.app.responses.BankResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BankPaymentResponse {

  @Schema(description = "Mã BIN", example = "123456")
  private String bin;

  @Schema(description = "Tên ngân hàng", example = "ABC Bank")
  private String bankName;

  @Schema(description = "Tên ngắn gọn của ngân hàng", example = "ABC")
  private String bankShortName;

  @Schema(description = "Số tài khoản ngân hàng", example = "1234567890")
  private String bankAccount;
}
