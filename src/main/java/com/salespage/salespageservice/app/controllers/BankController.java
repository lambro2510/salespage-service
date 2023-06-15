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
  public ResponseEntity<?> genQrCode(@RequestParam Long amount, Authentication authentication){
    try{

      return successApi(null, bankService.genTransactionQr(getUsername(authentication), amount));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("async-data")
  public ResponseEntity<?> asyncData(){
    try{
      bankService.asyncTransaction();
      return successApi(null, "Đồng bộ dữ liệu thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("create-payment")
  public ResponseEntity<?> createPayment(Authentication authentication){
    try{
      return successApi("Tạo giao dịch thành công.", bankService.createPayment(getUsername(authentication)));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("confirm-payment")
  public ResponseEntity<?> confirmPayment(Authentication authentication, @RequestParam String paymentId){
    try{
      return successApi(null, bankService.confirmPayment(getUsername(authentication), paymentId));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
