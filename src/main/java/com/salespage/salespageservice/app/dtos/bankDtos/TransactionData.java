package com.salespage.salespageservice.app.dtos.bankDtos;

import lombok.Data;

@Data
public class TransactionData {
  private Integer id;
  private String tid;
  private String description;
  private Long amount;
  private Integer cusumBalance;
  private String when;
  private String bankSubAccId;
  private String subAccId;
  private String virtualAccount;
  private String virtualAccountName;
  private String corresponsiveName;
  private String corresponsiveAccount;
  private String corresponsiveBankId;
  private String corresponsiveBankName;
}
