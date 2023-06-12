package com.salespage.salespageservice.app.dtos.bankDtos;

import lombok.Data;

import java.util.List;

@Data
public class BankDto {
  String error;

  List<TransactionData> data;
}
