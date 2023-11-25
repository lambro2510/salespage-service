package com.salespage.salespageservice.app.controllers.AdminController;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.dtos.productDtos.ProductTypeDetailDto;
import com.salespage.salespageservice.app.dtos.productDtos.ProductTypeDto;
import com.salespage.salespageservice.app.dtos.productDtos.UpdateTypeDetailStatusDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin product type", description = "Quản lý loại sản phẩm đăng bán")
@RequestMapping("api/v1/admin/product-type")
@RestController
@CrossOrigin
@SecurityRequirement(name = "bearerAuth")
public class ProductTypeController extends BaseController {

  @Autowired
  private ProductService productService;

  @PostMapping("type")
  @Operation(summary = "Tạo loại sản phẩm", description = "Tạo mới một loại sản phẩm")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Loại sản phẩm được tạo thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> createProductType(Authentication authentication, @RequestBody ProductTypeDto dto) {
    try {
      productService.createProductType(getUsername(authentication), dto, getUserRoles(authentication));
      return successApi("Tạo loại sản phẩm thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }

  }

  @PutMapping("type")
  @Operation(summary = "Cập nhật loại sản phẩm", description = "Cập nhật một loại sản phẩm")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Loại sản phẩm được cập nhật thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> updateProductType(Authentication authentication, @RequestBody ProductTypeDto dto) {
    try {
      productService.updateProductType(getUsername(authentication), dto, getUserRoles(authentication));
      return successApi("Cập nhật loại sản phẩm thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PostMapping("type-detail")
  @Operation(summary = "Tạo loại chi tiết sản phẩm", description = "Tạo mới một loại chi tiết sản phẩm")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Loại chi tiết sản phẩm được tạo thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> createProductTypeDetail(Authentication authentication, @RequestBody ProductTypeDetailDto dto) {
    try {
      productService.createProductTypeDetail(dto, getUsername(authentication));
      return successApi("Tạo loại chi tiết sản phẩm thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PutMapping("type-detail")
  @Operation(summary = "Cập nhật loại chi tiết sản phẩm", description = "Cập nhật loại chi tiết sản phẩm một loại sản phẩm")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cập nhật loại chi tiết sản phẩm được cập nhật thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> updateProductTypeDetail(Authentication authentication, @RequestBody ProductTypeDetailDto dto, @RequestParam String productTypeId) {
    try {
      productService.updateProductTypeDetail(dto, productTypeId, getUsername(authentication));
      return successApi("Cập nhật loại chi tiết sản phẩm thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @PutMapping("type-detail-status")
  @Operation(summary = "Cập nhật trạng thái loại chi tiết sản phẩm", description = "Cập nhật loại chi tiết sản phẩm một loại sản phẩm")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cập nhật loại chi tiết sản phẩm được cập nhật thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> updateProductTypeStatus(Authentication authentication, @RequestBody UpdateTypeDetailStatusDto dto) {
    try {
      productService.updateStatusTypeDetail(dto, getUsername(authentication), getUserRoles(authentication));
      return successApi("Cập nhật trạng thái loại chi tiết sản phẩm thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @GetMapping("type")
  public ResponseEntity<BaseResponse> getAllProductType(Authentication authentication, @RequestParam(required = false) String typeName) {
    try {
      return successApi(null, productService.getAllProductType(getUserRoles(authentication), typeName));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }
}
