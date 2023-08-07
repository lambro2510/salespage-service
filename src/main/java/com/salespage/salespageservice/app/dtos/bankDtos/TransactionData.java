package com.salespage.salespageservice.app.dtos.bankDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class TransactionData {

  @NotNull(message = "Ngày đăng ký là bắt buộc")
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  @Schema(description = "Ngày đăng ký giao dịch", required = true)
  private LocalDateTime postingDate;

  @NotNull(message = "Ngày giao dịch là bắt buộc")
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  @Schema(description = "Ngày giao dịch", required = true)
  private LocalDateTime transactionDate;

  @NotNull(message = "Số tài khoản là bắt buộc")
  @Schema(description = "Số tài khoản ngân hàng", required = true)
  private String accountNo;

  private Double creditAmount;
  private Double debitAmount;

  @Schema(description = "Đơn vị tiền tệ")
  private String currency;

  @Schema(description = "Mô tả giao dịch")
  private String description;

  private Double availableBalance;

  @Schema(description = "Số tài khoản người nhận")
  private String beneficiaryAccount;

  @Schema(description = "Số tham chiếu giao dịch")
  private String refNo;

  @Schema(description = "Tên tài khoản người nhận")
  private String benAccountName;

  @Schema(description = "Tên ngân hàng")
  private String bankName;

  @Schema(description = "Số tài khoản người nhận")
  private String benAccountNo;
}
