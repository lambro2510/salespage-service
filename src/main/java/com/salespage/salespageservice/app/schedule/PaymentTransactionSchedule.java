package com.salespage.salespageservice.app.schedule;

import com.salespage.salespageservice.domains.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/v1/it/payment-transaction")
public class PaymentTransactionSchedule {

  @Autowired
  BankService bankService;

  @PostMapping("mb/process-payment")
  public ResponseEntity<Boolean> processMbPayment() {
    bankService.processMbPayment();
    return ResponseEntity.ok(true);
  }

}
