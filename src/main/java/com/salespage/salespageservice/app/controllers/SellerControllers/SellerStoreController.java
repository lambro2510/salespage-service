package com.salespage.salespageservice.app.controllers.SellerControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.dtos.storeDtos.SellerStoreDto;
import com.salespage.salespageservice.app.dtos.storeDtos.UpdateSellerStoreDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
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

@Tag(name = "Seller store", description = "Quản lý sản phẩm được bán")
@CrossOrigin
@RestController
@RequestMapping("api/v1/seller/store")
@SecurityRequirement(name = "bearerAuth")
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
  public ResponseEntity<BaseResponse> getStore(Authentication authentication, Pageable pageable) {
    try {
      return successApi(sellerStoreService.getAllSellerStore(getUsername(authentication), pageable));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @GetMapping("{id}")
  @Operation(summary = "Lấy thông tin cửa hàng", description = "Lấy thông tin cửa hàng của người bán")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy cửa hàng"),
      @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<BaseResponse> getStoreDetail(Authentication authentication, @PathVariable String id) {
    try {
      return successApi(sellerStoreService.getStoreDetail(getUsername(authentication), id));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PostMapping("")
  @Operation(summary = "Tạo cửa hàng", description = "Tạo cửa hàng của người bán")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<BaseResponse> createStore(Authentication authentication, @RequestBody SellerStoreDto dto) {
    try {
      sellerStoreService.createStore(getUsername(authentication), dto);
      return successApi("Tạo cửa hàng thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PutMapping("{id}")
  @Operation(summary = "Cập nhật thông tin các cửa hàng", description = "Cập nhật thông tin các cửa hàng của người bán")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<BaseResponse> updateStore(Authentication authentication, @PathVariable String id, @RequestBody UpdateSellerStoreDto dto) {
    try {
      sellerStoreService.updateStore(getUsername(authentication), id, dto);
      return successApi("Cập nhật cửa hàng thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @DeleteMapping("")
  @Operation(summary = "Xóa cửa hàng", description = "Xóa cửa hàng của người bán")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<BaseResponse> deleteStore(Authentication authentication, @RequestParam String storeId) {
    try {
      sellerStoreService.deleteStore(getUsername(authentication), storeId);
      return successApi("Xóa cửa hàng thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PostMapping("upload-image")
  @Operation(summary = "Tải ảnh lên cho cửa hàng", description = "Tải ảnh lên cho cửa hàng")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
      @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
      @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<BaseResponse> uploadImage(Authentication authentication, @RequestParam String storeId, @RequestBody MultipartFile file) throws IOException {
    try {
      return successApi("Tải ảnh lên thành công", sellerStoreService.uploadImage(getUsername(authentication), storeId, file));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

}
