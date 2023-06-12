package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.bankDtos.BankDto;
import com.salespage.salespageservice.domains.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/bank")
public class BankController extends BaseController{

  @Autowired
  AccountService accountService;

  @PostMapping("")
  public ResponseEntity<?> receiveBankTransaction(@RequestBody BankDto bankDto) throws Exception {
    try{
      accountService.receiveBankTransaction(bankDto);
      return successApi("Lưu thông tin thành công");
    }catch (Exception ex){
      return errorApiStatus500("Không lưu được thông tin giao dịch");
    }
  }

  @GetMapping("")
  public ResponseEntity<?> getAllTransaction() throws Exception {
    try{
      return successApi(accountService.getAllTransaction());
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
