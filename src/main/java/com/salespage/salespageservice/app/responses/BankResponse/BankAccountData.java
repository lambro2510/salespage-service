package com.salespage.salespageservice.app.responses.BankResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BankAccountData {

  @Schema(description = "Tên tài khoản ngân hàng")
  private String accountName;

}
