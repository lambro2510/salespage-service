package com.salespage.salespageservice.domains.info;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TpBankTransaction {

  private List<TpBankTransactionInfo> transactionInfos;
  @Data
  public static class TpBankTransactionInfo{
    private String id;
    private String arrangementId;
    private String reference;
    private String description;
    private String category;
    private String bookingDate;
    private String valueDate;
    private String amount;
    private String currency;
    private String creditDebitIndicator;
    private String runningBalance;
    private String ofsAcctNo;
    private String creditorBankNameVn;
    private String creditorBankNameEn;
  }

}
