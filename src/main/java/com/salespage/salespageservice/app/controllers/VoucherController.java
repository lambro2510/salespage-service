package com.salespage.salespageservice.app.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.app.dtos.voucherDtos.VoucherStoreDto;
import com.salespage.salespageservice.domains.services.BaseService;
import com.salespage.salespageservice.domains.services.VoucherCodeService;
import com.salespage.salespageservice.domains.services.VoucherStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("v1/api/voucher")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Quản lý kho voucher cửa người dùng", description = "Quản lý voucher của người dùng")
public class VoucherController extends BaseController {
  @Autowired
  private VoucherStoreService voucherStoreService;

  @Autowired
  private VoucherCodeService voucherCodeService;
  @PostMapping("voucher-store")
  @Operation(summary = "Tạo mới một Voucher Store", description = "Tạo mới một Voucher Store với thông tin được cung cấp")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Tạo mới Voucher Store thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> createVoucherStore(Authentication authentication, @RequestBody VoucherStoreDto voucherStoreDto){
    return voucherStoreService.createVoucherStore(getUsername(authentication), voucherStoreDto);
  }

  @PutMapping("voucher-store")
  @Operation(summary = "Cập nhật thông tin một Voucher Store", description = "Cập nhật thông tin một Voucher Store với thông tin được cung cấp")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Cập nhật thông tin Voucher Store thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "404", description = "Không tìm thấy Voucher Store"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> updateVoucherStore(Authentication authentication, @RequestBody VoucherStoreDto voucherStoreDto, @RequestParam String voucherStoreId){
    return voucherStoreService.updateVoucherStore(getUsername(authentication), voucherStoreDto, voucherStoreId);
  }

  @DeleteMapping("voucher-store")
  @Operation(summary = "Xóa một Voucher Store", description = "Xóa một Voucher Store theo id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Xóa Voucher Store thành công"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "404", description = "Không tìm thấy Voucher Store"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> deleteVoucherStore( Authentication authentication, @RequestParam String voucherStoreId){
    return voucherStoreService.deleteVoucherStore(getUsername(authentication), voucherStoreId);
  }

  @GetMapping("voucher-store")
  @Operation(summary = "Lấy danh sách Voucher Store", description = "lấy toàn bộ Voucher Store theo người dùng")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lấy danh sách Voucher Store thành công"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> getAllVoucherStore(Authentication authentication){
    return voucherStoreService.getAllVoucherStore(getUsername(authentication));
  }

  @GetMapping("voucher-code")
  @Operation(summary = "Tạo các voucher code", description = "Tạo ngẫu nhiên danh sách các voucher code")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tạo mã code thành công"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy Voucher Store"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> createVoucherCode(Authentication authentication, @RequestParam String voucherStoreId, @RequestParam Long numberVoucher, @RequestParam(required = false) @JsonFormat(pattern = "dd-MM-yyyy") Date expireTime){
    return voucherCodeService.generateVoucherCode(getUsername(authentication),voucherStoreId, numberVoucher, expireTime);
  }
}
