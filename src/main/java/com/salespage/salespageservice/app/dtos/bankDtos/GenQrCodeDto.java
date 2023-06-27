package com.salespage.salespageservice.app.dtos.bankDtos;

import lombok.Data;

@Data
public class GenQrCodeDto {

    Long accountNo;

    String accountName;

    Long acqId;

    Long amount;

    String addInfo;

    String format;

    String template;
}
