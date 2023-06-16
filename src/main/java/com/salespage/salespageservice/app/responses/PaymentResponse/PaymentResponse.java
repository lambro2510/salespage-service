package com.salespage.salespageservice.app.responses.PaymentResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentResponse {

  String paymentId;

  String bankName;

  String bankAccountName;

  String amount;

  @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
  Date createdAt;
}
