package com.salespage.salespageservice.app.responses.BankResponse;

import lombok.Data;

@Data
public class BankPaymentResponse {
  String bin;

  String bankName;

  String bankShortName;
}
