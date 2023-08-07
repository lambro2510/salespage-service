package com.salespage.salespageservice.app.responses.BankResponse;

import com.salespage.salespageservice.domains.entities.BankAccount;
import com.salespage.salespageservice.domains.entities.status.BankStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BankAccountResponse {

  @Schema(description = "ID tài khoản ngân hàng", example = "60f7c4468c1de5f1a450b589")
  private String bankAccountId;

  @Schema(description = "Tên người dùng", example = "JohnDoe")
  private String username;

  @Schema(description = "Số tài khoản ngân hàng", example = "1234567890")
  private String accountNo;

  @Schema(description = "ID ngân hàng", example = "1")
  private Long bankId;

  @Schema(description = "Mã BIN", example = "123456")
  private String bin;

  @Schema(description = "Tên ngân hàng", example = "ABC Bank")
  private String bankName;

  @Schema(description = "URL logo ngân hàng", example = "https://example.com/bank-logo.jpg")
  private String bankLogoUrl;

  @Schema(description = "Tên đầy đủ ngân hàng", example = "ABC Bank Corporation")
  private String bankFullName;

  @Schema(description = "Tên chủ tài khoản ngân hàng", example = "John Doe")
  private String bankAccountName;

  @Schema(description = "Trạng thái ngân hàng", example = "ACTIVE")
  private BankStatus status;

  @Schema(description = "Số tiền gửi vào", example = "5000.0")
  private Double moneyIn;

  @Schema(description = "Số tiền rút ra", example = "3000.0")
  private Double moneyOut;

  public void assignFromBankAccount(BankAccount bankAccount) {
    bankAccountId = bankAccount.getId().toHexString();
    username = bankAccount.getUsername();
    accountNo = bankAccount.getAccountNo();
    bankId = bankAccount.getBankId();
    bin = bankAccount.getBin();
    bankName = bankAccount.getBankName();
    bankLogoUrl = bankAccount.getBankLogoUrl();
    bankFullName = bankAccount.getBankFullName();
    bankAccountName = bankAccount.getBankAccountName();
    status = bankAccount.getStatus();
    moneyIn = bankAccount.getMoneyIn();
    moneyOut = bankAccount.getMoneyOut();
  }
}
