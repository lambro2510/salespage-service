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

    @PostMapping("")
    public ResponseEntity<?> receiveBankTransaction(@RequestBody BankDto bankDto) throws Exception {
        try {
            bankService.receiveBankTransaction(bankDto);
            return successApi("Lưu thông tin thành công");
        } catch (Exception ex) {
            return errorApiStatus500("Không lưu được thông tin giao dịch");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllTransaction() throws Exception {
        try {
            return successApi(bankService.getAllTransaction());
        } catch (Exception ex) {
            return errorApi(ex.getMessage());
        }
    }

    @PostMapping("gen-qr")
    public ResponseEntity<?> genQrCode(@RequestParam String paymentId, Authentication authentication) {
        try {
            return successApi(null, bankService.genTransactionQr(getUsername(authentication), paymentId));
        } catch (Exception ex) {
            return errorApi(ex.getMessage());
        }
    }

    @GetMapping("list-bank")
    @Operation(summary = "Get List of Banks", description = "Retrieve a list of banks")
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
    @Operation(summary = "Thông tin tài khoản ngân hang", description = "Thông tin tài khoản ngân hàng")
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
    public ResponseEntity<?> asyncData() {
        try {
            bankService.asyncTransaction();
            return successApi(null, "Đồng bộ dữ liệu thành công");
        } catch (Exception ex) {
            return errorApi(ex.getMessage());
        }
    }

    @PostMapping("link-bank-account")
    public ResponseEntity<?> linkBankAccount(Authentication authentication, @RequestBody BankAccountInfoRequest request) {
        try {
            bankService.linkBankAccount(getUsername(authentication), request);
            return successApi("Liên kết với tài khoản thành công");
        } catch (Exception ex) {
            return errorApi(ex.getMessage());
        }
    }

}
