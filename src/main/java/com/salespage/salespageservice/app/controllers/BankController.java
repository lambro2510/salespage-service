package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.bankDtos.BankAccountInfoRequest;
import com.salespage.salespageservice.app.dtos.bankDtos.BankDto;
import com.salespage.salespageservice.app.responses.BankResponse.BankAccountData;
import com.salespage.salespageservice.app.responses.swaggerResponse.BankListDataRes;
import com.salespage.salespageservice.domains.services.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/bank")
public class BankController extends BaseController {

  @Autowired
  BankService bankService;

  @GetMapping("")
  @Operation(summary = "Get All Transactions", description = "Retrieve a list of all bank transactions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getAllTransaction() throws Exception {
    try {
      return successApi(bankService.getAllTransaction());
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("mb-bank-transaction")
  @Operation(summary = "Get All Transactions", description = "Retrieve a list of all bank transactions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getMbBankTransaction() throws Exception {
    try {
      return successApi(bankService.getMbBankTransaction());
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }


  @PostMapping("gen-qr")
  @Operation(summary = "Generate QR Code", description = "Generate a QR code for a specific payment")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> genQrCode(@RequestParam String paymentId, @RequestParam String bin, @RequestParam String bankAccountId, Authentication authentication) {
    try {
      return successApi(null, bankService.genTransactionQr(getUsername(authentication),bin,bankAccountId, paymentId));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("list-bank")
  @Operation(summary = "Danh sách ngân hàng", description = "Lấy danh sách ngân hàng")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankListDataRes.class)))),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getListBank() {
    try {
      return successApi(null, bankService.getListBank());
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("account-info")
  @Operation(summary = "Thông tin tài khoản ngân hàng", description = "Thông tin tài khoản ngân hàng")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BankAccountData.class))),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getBankAccountData(@RequestParam String bin, @RequestParam String accountNo) {
    try {
      return successApi(null, bankService.getBankAccountData(bin, accountNo));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("async-data")
  @Operation(summary = "Asynchronous Data Synchronization", description = "Synchronize bank transaction data asynchronously")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> asyncData() {
    try {
      bankService.asyncTransaction();
      return successApi(null, "Đồng bộ dữ liệu thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("link-bank-account")
  @Operation(summary = "Link Bank Account", description = "Link a bank account with the user's account")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> linkBankAccount(Authentication authentication, @RequestBody BankAccountInfoRequest request) {
    try {
      bankService.linkBankAccount(getUsername(authentication), request);
      return successApi("Liên kết với tài khoản thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("link-bank-account")
  @Operation(summary = "Danh sách tài khoản liên kêt", description = "Lấy danh sách ngân hàng liên kết")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getLinkBankAccount(Authentication authentication) {
    try {
      return successApi(bankService.getBankAccount(getUsername(authentication)));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("payment-bank-account")
  @Operation(summary = "Danh sách ngân hàng có thể sử dụng để nạp tiền", description = "Danh sách ngân hàng có thể sử dụng để nạp tiền")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getPaymentBankAccount() {
    try {
      return successApi(bankService.getPaymentBankAccount());
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }


}
