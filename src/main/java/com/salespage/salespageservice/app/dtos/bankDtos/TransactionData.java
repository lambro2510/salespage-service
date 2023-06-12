package com.salespage.salespageservice.app.dtos.bankDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionData {
  private Integer id;
  private String tid;
  private String description;
  private Integer amount;
  private Integer cusumBalance;
  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private LocalDateTime when;
  private String bankSubAccId;
  private String subAccId;
  private String virtualAccount;
  private String virtualAccountName;
  private String corresponsiveName;
  private String corresponsiveAccount;
  private String corresponsiveBankId;
  private String corresponsiveBankName;
}
