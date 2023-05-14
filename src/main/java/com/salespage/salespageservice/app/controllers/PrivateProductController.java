package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.productDtos.*;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductType;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Quản lý sản phẩm", description = "Quản lý sản phẩm người dùng bán")
@CrossOrigin
@RestController
@RequestMapping("v1/api/product")
@SecurityRequirement(name = "bearerAuth")
public class PrivateProductController extends BaseController {

  @Autowired
  private ProductService productService;

  @PostMapping("")
  @Operation(summary = "Tạo sản phẩm", description = "Tạo sản phẩm mới với thông tin đã cho")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Sản phẩm tạo thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<List<Product>> createProduct(Authentication authentication, @RequestBody List<ProductInfoDto> dto) {
    return productService.createProduct(getUsername(authentication), dto);
  }


  @PostMapping("upload-images")
  @Operation(summary = "Tải lên hình ảnh cho sản phẩm", description = "Tải lên một hoặc nhiều hình ảnh cho sản phẩm với ID đã cho")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Hình ảnh tải lên thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "404", description = "Không tòn tại sản phẩm này hoặc đã bị xóa"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<List<String>> uploadImages(Authentication authentication, @RequestParam String productId, @RequestParam List<MultipartFile> files) throws IOException {
    return productService.uploadProductImage(getUsername(authentication), productId, files);
  }


  @DeleteMapping("delete-images")
  @Operation(summary = "Xóa hình ảnh cho sản phẩm", description = "Xóa một hoặc nhiều hình ảnh cho sản phẩm với ID đã cho")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Hình ảnh xóa thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<List<String>> deleteImages(Authentication authentication, @RequestParam String productId, @RequestParam List<String> imageIds) {
    return productService.deleteProductImages(getUsername(authentication), productId, imageIds);
  }


  @DeleteMapping("")
  @Operation(summary = "Xóa sản phẩm", description = "Xóa sản phẩm với ID đã cho")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Sản phẩm đã được xóa thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<Boolean> deleteProduct(Authentication authentication, @RequestParam String productId) throws IOException {
    return productService.deleteProduct(getUsername(authentication), productId);
  }


  @PutMapping("")
  @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật sản phẩm với thông tin đã cho")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Sản phẩm được cập nhật thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "404", description = "Không tòn tại sản phẩm này hoặc đã bị xóa"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<Product> updateProduct(Authentication authentication, @RequestBody ProductDto dto) {
    return productService.updateProduct(getUsername(authentication), dto);
  }

  @PostMapping("type")
  @Operation(summary = "Tạo loại sản phẩm", description = "Tạo mới một loại sản phẩm")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Loại sản phẩm được tạo thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> createProductType(Authentication authentication, @RequestBody ProductTypeDto dto) {
    return productService.createProductType(getUsername(authentication), dto, getUserRoles(authentication));
  }

  @PutMapping("type")
  @Operation(summary = "Cập nhật loại sản phẩm", description = "Cập nhật một loại sản phẩm")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Loại sản phẩm được cập nhật thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> updateProductType(Authentication authentication, @RequestBody ProductTypeDto dto) {
    return productService.updateProductType(getUsername(authentication), dto, getUserRoles(authentication));
  }

  @PostMapping("type-detail")
  @Operation(summary = "Tạo loại chi tiết sản phẩm", description = "Tạo mới một loại chi tiết sản phẩm")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Loại chi tiết sản phẩm được tạo thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> createProductTypeDetail(Authentication authentication, @RequestBody ProductTypeDetailDto dto) {
    return productService.createProductTypeDetail(dto, getUsername(authentication));
  }

  @PutMapping("type-detail")
  @Operation(summary = "Cập nhật loại chi tiết sản phẩm", description = "Cập nhật loại chi tiết sản phẩm một loại sản phẩm")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Cập nhật loại chi tiết sản phẩm được cập nhật thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> updateProductTypeDetail(Authentication authentication, @RequestBody ProductTypeDetailDto dto, @RequestParam String productTypeId) {
    return productService.updateProductTypeDetail(dto, productTypeId, getUsername(authentication));
  }

  @PutMapping("type-detail-status")
  @Operation(summary = "Cập nhật loại chi tiết sản phẩm", description = "Cập nhật loại chi tiết sản phẩm một loại sản phẩm")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Cập nhật loại chi tiết sản phẩm được cập nhật thành công"),
          @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
          @ApiResponse(responseCode = "401", description = "Không được phép"),
          @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> updateProductTypeDetail(Authentication authentication, @RequestBody UpdateTypeDetailStatusDto dto) {
    return productService.updateStatusTypeDetail(dto, getUsername(authentication), getUserRoles(authentication));
  }

  @GetMapping("type")
  public ResponseEntity<List<ProductType>> getAllProductType(Authentication authentication) {
    return productService.getAllProductType(getUserRoles(authentication));
  }

}
