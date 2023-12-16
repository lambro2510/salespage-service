package com.salespage.salespageservice.app.controllers.SellerControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
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
@RequestMapping("api/v1/seller/voucher")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Seller voucher", description = "Quản lý voucher của người dùng")
public class SellerVoucherController extends BaseController {
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
  public ResponseEntity<BaseResponse> createVoucherStore(Authentication authentication, @RequestBody CreateVoucherStoreDto updateVoucherStoreDto) {
    try {
      voucherStoreService.createVoucherStore(getUsername(authentication), updateVoucherStoreDto);
      return successApi("Tạo kho voucher mới thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
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
  public ResponseEntity<BaseResponse> updateVoucherStore(Authentication authentication, @RequestBody UpdateVoucherStoreDto updateVoucherStoreDto, @RequestParam String voucherStoreId) {
    try {
      voucherStoreService.updateVoucherStore(getUsername(authentication), updateVoucherStoreDto, voucherStoreId);
      return successApi("Cập nhật kho voucher thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @DeleteMapping("voucher-store")
  @Operation(summary = "Xóa một Voucher Store", description = "Xóa một Voucher Store theo id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Xóa Voucher Store thành công"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy Voucher Store"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> deleteVoucherStore(Authentication authentication, @RequestParam String voucherStoreId) {
    try {
      voucherStoreService.deleteVoucherStore(getUsername(authentication), voucherStoreId);
      return successApi("Xóa kho voucher thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @GetMapping("voucher-store")
  @Operation(summary = "Lấy danh sách Voucher Store", description = "lấy toàn bộ Voucher Store theo người dùng")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lấy danh sách Voucher Store thành công"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> getAllVoucherStore(Authentication authentication, Pageable pageable) {
    try {
      return successApi(voucherStoreService.getAllVoucherStore(getUsername(authentication), pageable));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PostMapping("voucher-code")
  @Operation(summary = "Tạo các voucher code", description = "Tạo ngẫu nhiên danh sách các voucher code")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tạo mã code thành công"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy Voucher Store"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> createVoucherCode(Authentication authentication,
                                                        @RequestParam String voucherStoreId,
                                                        @RequestParam Long numberVoucher,
                                                        @RequestParam(required = false) @Schema(type = "string", format = "date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate expireTime) {
    try {
      voucherCodeService.generateVoucherCode(getUsername(authentication), voucherStoreId, numberVoucher, expireTime);
      return successApi("Tạo mã voucher mới thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @GetMapping("voucher-code")
  @Operation(summary = "Lấy các mã voucher code", description = "Lấy toàn bộ các mã voucher trong store")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lấy mã code thành công"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy Voucher Store"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> getAllVoucherCode(Authentication authentication, @RequestParam String voucherStoreId, @RequestParam(required = false) VoucherCodeStatus voucherCodeStatus, Pageable pageable) {
    try {
      return successApi(voucherCodeService.getAllVoucherCodeInStore(getUsername(authentication), voucherStoreId, voucherCodeStatus, pageable));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

}
