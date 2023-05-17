package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.productDtos.*;
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
  public ResponseEntity<BaseResponse> createProduct(Authentication authentication, @RequestBody List<ProductInfoDto> dto) {
    try {
      return successApi("Tạo sản phẩm thành công", productService.createProduct(getUsername(authentication), dto));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
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
  public ResponseEntity<BaseResponse> uploadImages(Authentication authentication, @RequestParam String productId, @RequestParam List<MultipartFile> files) throws IOException {
    try {
      return successApi("Tải ảnh lên thành công", productService.uploadProductImage(getUsername(authentication), productId, files));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
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
  public ResponseEntity<BaseResponse> deleteImages(Authentication authentication, @RequestParam String productId, @RequestParam String imageIds) {
    try {
      return successApi("Xóa ảnh thành công", productService.deleteProductImages(getUsername(authentication), productId, imageIds));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
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
  public ResponseEntity<BaseResponse> deleteProduct(Authentication authentication, @RequestParam String productId) throws IOException {
    try {
      return successApi("Xóa sản phẩm thành công", productService.deleteProduct(getUsername(authentication), productId));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
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
  public ResponseEntity<BaseResponse> updateProduct(Authentication authentication, @RequestBody ProductDto dto) {
    try {
      return successApi("Cập nhật sản phẩm thành công", productService.updateProduct(getUsername(authentication), dto));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

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
      return errorApi(ex.getMessage());
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
      return errorApi(ex.getMessage());
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
      return errorApi(ex.getMessage());
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
      return errorApi(ex.getMessage());
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
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("type")
  public ResponseEntity<BaseResponse> getAllProductType(Authentication authentication) {
    try {
      return successApi(null, productService.getAllProductType(getUserRoles(authentication)));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

}
