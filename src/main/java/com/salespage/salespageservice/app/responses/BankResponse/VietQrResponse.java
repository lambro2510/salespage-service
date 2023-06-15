package com.salespage.salespageservice.app.responses.BankResponse;

import lombok.Data;

@Data
public class VietQrResponse {
  private String code;

  private String desc;

  private QrData data;
}
