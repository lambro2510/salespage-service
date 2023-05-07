package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.SellerStoreDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.storeResponse.StoreDataResponse;
import com.salespage.salespageservice.domains.services.SellerStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("v1/api/seller-store")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Quản lý cửa hàng của người dùng", description = "Quản lý cửa hàng của người dùng")
public class SellerStoreController extends BaseController {

  @Autowired
  private SellerStoreService sellerStoreService;

  @GetMapping("")
  @Operation(summary = "Lấy thông tin các cửa hàng", description = "Lấy thông tin các cửa hàng của người bán")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Thành công"),
          @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
          @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
          @ApiResponse(responseCode = "404", description = "Không tìm thấy cửa hàng"),
          @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<PageResponse<StoreDataResponse>> getStore(Authentication authentication, Pageable pageable) {
    return sellerStoreService.getAllStore(getUsername(authentication), pageable);
  }

  @PostMapping("")
  @Operation(summary = "Lấy thông tin các cửa hàng", description = "Lấy thông tin các cửa hàng của người bán")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Thành công"),
          @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
          @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
          @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<?> createStore(Authentication authentication, @RequestBody SellerStoreDto dto) {
    return sellerStoreService.createStore(getUsername(authentication), dto);
  }

  @PostMapping("upload-image")
  @Operation(summary = "Tải ảnh lên cho cửa hàng", description = "Tải ảnh lên cho cửa hàng")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Thành công"),
          @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
          @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
          @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<?> uploadImage(Authentication authentication, @RequestParam String storeId, @RequestParam MultipartFile multipartFile) throws IOException {
    return sellerStoreService.uploadImage(getUsername(authentication), storeId, multipartFile);
  }
}
