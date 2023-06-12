package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.dtos.bankDtos.BankDto;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDetailResponse;
import com.salespage.salespageservice.domains.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController()
@RequestMapping("v1/api/public/bank")
public class BankController {

  @Autowired
  AccountService accountService;

  @GetMapping("")
  public ResponseEntity<?> receiveBankTransaction(@RequestBody BankDto bankDto) throws Exception {
    return accountService.receiveBankTransaction(bankDto);
  }
}
