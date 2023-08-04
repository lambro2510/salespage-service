package com.salespage.salespageservice.app.dtos.bankDtos;

import lombok.Data;

@Data
public class GenQrCodeDto {

  String accountNo;

  String accountName;

  String acqId;

  Long amount;

  String addInfo;

  String format;

  String template;
}
