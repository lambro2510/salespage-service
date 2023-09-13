package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.PaymentDtos.CreatePaymentDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.app.responses.InfoResponse;
import com.salespage.salespageservice.domains.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/payment")
public class PaymentController extends BaseController {

  @Autowired
  private PaymentService paymentService;

  @GetMapping("")
  @Operation(summary = "Lịch sử nạp và rút tiền", description = "Truy vấn lịch sử các giao dịch nạp và rút tiền")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Không được ủy quyền, vui lòng kiểm tra thông tin xác thực của bạn"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> getPaymentTransaction(Authentication authentication, Pageable pageable) {
    try {
      return successApi(paymentService.getPayment(getUsername(authentication), pageable));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("")
  @Operation(summary = "Tạo giao dịch", description = "Tạo một giao dịch nạp hoặc rút tiền mới")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> createPayment(Authentication authentication, @RequestBody CreatePaymentDto dto) {
    try {
      return successApi("Tạo giao dịch thành công.", paymentService.createPayment(getUsername(authentication), dto));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("confirm")
  @Operation(summary = "Xác nhận giao dịch", description = "Xác nhận một giao dịch nạp hoặc rút tiền")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> confirmPayment(Authentication authentication, @RequestParam String paymentId) {
    try {
      InfoResponse response = paymentService.confirmPayment(getUsername(authentication), paymentId);
      if (response.getCode() == 0) {
        return successApi(null, response.getMessage());
      } else if (response.getCode() == 1) {
        return errorApi(response.getCode(), response.getMessage());
      } else {
        return errorApi(response.getCode(), response.getMessage());
      }

    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("cancel")
  @Operation(summary = "Hủy giao dịch", description = "Hủy bỏ một giao dịch nạp hoặc rút tiền")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> cancelPayment(Authentication authentication, @RequestParam String paymentId) {
    try {
      paymentService.cancelPayment(getUsername(authentication), paymentId);
      return successApi("Hủy bỏ giao dịch thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }
}
