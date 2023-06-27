package com.salespage.salespageservice.app.dtos.bankDtos;

import lombok.Data;

import java.util.List;

@Data
public class BankDto {
    Integer error;

    List<TransactionData> data;
}
