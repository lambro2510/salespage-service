package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.voucherDtos.CreateVoucherStoreDto;
import com.salespage.salespageservice.app.dtos.voucherDtos.UpdateVoucherStoreDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import com.salespage.salespageservice.domains.services.VoucherCodeService;
import com.salespage.salespageservice.domains.services.VoucherStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin
@RequestMapping("api/v1/voucher")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Voucher", description = "Quản lý voucher của người dùng")
public class VoucherController extends BaseController {
  @Autowired
  private VoucherStoreService voucherStoreService;

  @Autowired
  private VoucherCodeService voucherCodeService;

  @GetMapping("receive/voucher-code")
  @Operation(summary = "Nhận các mã voucher code", description = "Nhận mã voucher trong store")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lấy mã code thành công"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy Voucher Store"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> receiveVoucher(Authentication authentication, @RequestParam String voucherStoreId) {
    try {
      return successApi("Nhận mã voucher thành công", voucherCodeService.receiveVoucher(getUsername(authentication), voucherStoreId));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }



  @GetMapping("user/voucher")
  public ResponseEntity<BaseResponse> getUserVoucher(Authentication authentication, @RequestParam String productId) {
    try {
      return successApi(voucherCodeService.getUserVoucher(getUsername(authentication), productId));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }
}
