package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.bankDtos.BankDto;
import com.salespage.salespageservice.domains.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/bank")
public class BankController extends BaseController{

  @Autowired
  BankService bankService;

  @PostMapping("")
  public ResponseEntity<?> receiveBankTransaction(@RequestBody BankDto bankDto) throws Exception {
    try{
      bankService.receiveBankTransaction(bankDto);
      return successApi("Lưu thông tin thành công");
    }catch (Exception ex){
      return errorApiStatus500("Không lưu được thông tin giao dịch");
    }
  }

  @GetMapping("")
  public ResponseEntity<?> getAllTransaction() throws Exception {
    try{
      return successApi(bankService.getAllTransaction());
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("gen-qr")
  public ResponseEntity<?> genQrCode(){
    try{
      return successApi(null, bankService.genTransactionQr());
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("async-data")
  public ResponseEntity<?> asyncData(Authentication authentication){
    try{

      bankService.asyncTransaction(getUsername(authentication));
      return successApi(null, "Đồng bộ dữ liệu thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("oath2Token")
  public ResponseEntity<?> getToken(Authentication authentication){
    try{
      return successApi(null, bankService.getOath2Token());
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
