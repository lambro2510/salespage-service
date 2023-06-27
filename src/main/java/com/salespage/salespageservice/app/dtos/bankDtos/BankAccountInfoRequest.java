package com.salespage.salespageservice.app.dtos.bankDtos;

import lombok.Data;

@Data
public class BankAccountInfoRequest {
    String bin;
    String accountNumber;
}
