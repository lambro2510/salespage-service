package com.salespage.salespageservice.app.dtos.bankDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountInfoRequest {

  @NotBlank(message = "BIN là bắt buộc")
  @Schema(description = "Số BIN của tài khoản ngân hàng")
  private String bin;

  @NotBlank(message = "Số tài khoản là bắt buộc")
  @Schema(description = "Số tài khoản ngân hàng")
  private String accountNumber;
}
