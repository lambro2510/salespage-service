package com.salespage.salespageservice.app.dtos.PaymentDtos;

import lombok.Data;

@Data
public class CreatePaymentDto {
    String bankAccountId;

    Long amount;
}
