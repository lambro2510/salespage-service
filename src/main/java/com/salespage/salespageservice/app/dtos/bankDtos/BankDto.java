package com.salespage.salespageservice.app.dtos.bankDtos;

import lombok.Data;

@Data
public class BankDto {
  String error;
  TransactionData data;
}
