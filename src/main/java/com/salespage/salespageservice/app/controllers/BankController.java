package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.bankDtos.BankDto;
import com.salespage.salespageservice.domains.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/bank")
public class BankController {

  @Autowired
  AccountService accountService;

  @GetMapping("")
  public ResponseEntity<?> receiveBankTransaction(@RequestBody BankDto bankDto) throws Exception {
    return accountService.receiveBankTransaction(bankDto);
  }
}
