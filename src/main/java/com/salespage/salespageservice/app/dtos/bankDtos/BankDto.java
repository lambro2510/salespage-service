package com.salespage.salespageservice.app.dtos.bankDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BankDto {

  @NotNull(message = "Lỗi là bắt buộc")
  @Schema(description = "Mã lỗi", required = true)
  private Integer error;

  @NotNull(message = "Dữ liệu giao dịch là bắt buộc")
  @Valid
  @Schema(description = "Dữ liệu giao dịch", required = true)
  private List<TransactionData> data;
}
