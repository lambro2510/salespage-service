package com.salespage.salespageservice.app.responses.PaymentResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentResponse {

  @Schema(description = "ID thanh toán")
  private String paymentId;

  @Schema(description = "Tên ngân hàng")
  private String bankName;

  @Schema(description = "Tên chủ tài khoản ngân hàng")
  private String bankAccountName;

  @Schema(description = "Số tiền thanh toán")
  private String amount;

  @Schema(description = "Ngày tạo thanh toán", example = "2023-08-07 10:30:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createdAt;
}
