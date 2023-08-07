package com.salespage.salespageservice.app.responses.BankResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class MbBankTransaction {

  boolean success;

  String message;

  List<Transaction> data;

  @Data
  public static class Transaction {

    private String postingDate;
    private String transactionDate;
    private String dueDate;
    private String accountNo;
    private Double creditAmount;
    private Double debitAmount;
    private String currency;
    private String description;
    private Double availableBalance;
    private String beneficiaryAccount;
    private String refNo;
    private String benAccountName;
    private String bankName;
    private String benAccountNo;
    private String transactionType;
    private String docId;
  }
  
}
