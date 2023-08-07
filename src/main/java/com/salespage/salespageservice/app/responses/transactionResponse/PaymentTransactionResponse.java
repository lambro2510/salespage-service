package com.salespage.salespageservice.app.responses.transactionResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentTransactionResponse {

  @Schema(description = "ID giao dịch thanh toán")
  String paymentId;

  @Schema(description = "Trạng thái thanh toán")
  PaymentStatus status;

  @Schema(description = "Tên ngân hàng")
  String bankName;

  @Schema(description = "Số tài khoản ngân hàng")
  String bankAccountNo;

  @Schema(description = "Tên chủ tài khoản ngân hàng")
  String bankAccountName;

  @Schema(description = "Số tiền")
  Long amount;

  @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
  @Schema(description = "Ngày tạo")
  Date created;
}
