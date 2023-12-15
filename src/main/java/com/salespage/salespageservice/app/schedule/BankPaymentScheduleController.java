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
@RequestMapping("api/v1/it/bank")
public class BankPaymentScheduleController {

  @Autowired
  BankService bankService;

  @PostMapping("tp/today")
  public ResponseEntity<Boolean> updatePaymentToday() throws Exception {
    bankService.saveTpBankTransactionToday();
    return ResponseEntity.ok(true);
  }

  @PostMapping("tp/pre")
  public ResponseEntity<Boolean> updatePaymentPre() throws Exception {
    bankService.saveTpBankTransactionPeriodDay();
    return ResponseEntity.ok(true);
  }

  @PostMapping("mb/today")
  public ResponseEntity<Boolean> updateMbPaymentToday() throws Exception {
    bankService.saveBankTransaction();
    return ResponseEntity.ok(true);
  }

  @PostMapping("mb/process-payment")
  public ResponseEntity<Boolean> processMbPayment() throws Exception {
    bankService.saveBankTransaction();
    return ResponseEntity.ok(true);
  }
}
