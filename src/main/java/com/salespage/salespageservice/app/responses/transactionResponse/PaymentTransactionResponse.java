package com.salespage.salespageservice.app.responses.transactionResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentTransactionResponse {
  String paymentId;

  PaymentStatus status;

  Long amount;

  @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
  Date created;
}
