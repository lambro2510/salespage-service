package com.salespage.salespageservice.app.dtos.bankDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

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
