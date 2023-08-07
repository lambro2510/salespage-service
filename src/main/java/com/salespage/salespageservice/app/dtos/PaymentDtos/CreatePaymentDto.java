package com.salespage.salespageservice.app.dtos.PaymentDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreatePaymentDto {

  @NotBlank(message = "ID tài khoản ngân hàng là bắt buộc")
  @Schema(description = "ID tài khoản ngân hàng", required = true)
  private String bankAccountId;

  @NotNull(message = "Số tiền là bắt buộc")
  @Schema(description = "Số tiền", required = true)
  private Long amount;
}
