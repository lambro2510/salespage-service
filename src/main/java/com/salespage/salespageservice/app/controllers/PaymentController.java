package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.PaymentDtos.CreatePaymentDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/payment")
public class PaymentController extends BaseController{

  @Autowired
  private BankService bankService;

  @GetMapping("payment-transaction")
  @Operation(summary = "Lịch sử nạp và rút tiền", description = "Lịch sử nạp và rút tiền")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Không được ủy quyền, vui lòng kiểm tra thông tin xác thực của bạn"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> getPaymentTransaction(Authentication authentication) {
    try {
      return successApi(bankService.getPayment(getUsername(authentication)));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("create-payment")
  public ResponseEntity<?> createPayment(Authentication authentication, @RequestBody CreatePaymentDto dto){
    try{
      return successApi("Tạo giao dịch thành công.", bankService.createPayment(getUsername(authentication), dto));
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

  @PutMapping("cancel-payment")
  public ResponseEntity<?> cancelPayment(Authentication authentication, @RequestParam String paymentId){
    try{
      bankService.cancelPayment(getUsername(authentication), paymentId);
      return successApi("Hủy bỏ giao dịch thành công" );
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }


}
