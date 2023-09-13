package com.salespage.salespageservice.app.controllers.SellerControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.dtos.productDtos.CreateProductInfoDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.ProductService;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Objects;

@Tag(name = "Seller product", description = "Quản lý sản phẩm được bán")
@CrossOrigin
@RestController
@RequestMapping("api/v1/seller/product")
@SecurityRequirement(name = "bearerAuth")
public class SellerProductController extends BaseController {
  @Autowired
  private ProductService productService;

  @GetMapping("")
  @Operation(summary = "Tạo sản phẩm", description = "Tạo sản phẩm mới với thông tin đã cho")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Sản phẩm tạo thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> getAllProduct(Authentication authentication,
                                                    @RequestParam(required = false) String storeName,
                                                    Pageable pageable) {
    try {
      return successApi(productService.getAllProduct(getUsername(authentication), storeName, pageable));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @GetMapping("detail/{productId}")
  public ResponseEntity<BaseResponse> getProductDetail(Authentication authentication, @PathVariable String productId) {
    try {
      return successApi(productService.getSellerProductDetail(productId));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("")
  @Operation(summary = "Tạo sản phẩm", description = "Tạo sản phẩm mới với thông tin đã cho")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Sản phẩm tạo thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> createProduct(Authentication authentication, @RequestBody @Valid CreateProductInfoDto dto) {
    try {
      return successApi("Tạo sản phẩm thành công", productService.createProduct(getUsername(authentication), dto));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("upload/{productId}")
  @Operation(summary = "Tải lên hình ảnh cho sản phẩm", description = "Tải lên một hoặc nhiều hình ảnh cho sản phẩm với ID đã cho")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hình ảnh tải lên thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tòn tại sản phẩm này hoặc đã bị xóa"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<?> uploadImages(Authentication authentication,
                                        @PathVariable String productId,
                                        @RequestBody @Valid @NotNull MultipartFile file) {
    try {
      return ResponseEntity.ok(productService.uploadProductImage(getUsername(authentication), productId, file));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("delete-images/{productId}")
  @Operation(summary = "Xóa hình ảnh cho sản phẩm", description = "Xóa một hoặc nhiều hình ảnh cho sản phẩm với ID đã cho")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hình ảnh xóa thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> deleteImages(Authentication authentication, @PathVariable String productId, @RequestParam String imageIds) {
    try {
      return successApi("Xóa ảnh thành công", productService.deleteProductImages(getUsername(authentication), productId, imageIds));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }


  @DeleteMapping("{productId}")
  @Operation(summary = "Xóa sản phẩm", description = "Xóa sản phẩm với ID đã cho")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Sản phẩm đã được xóa thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> deleteProduct(Authentication authentication, @PathVariable String productId) throws IOException {
    try {
      return successApi("Xóa sản phẩm thành công", productService.deleteProduct(getUsername(authentication), productId));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("{productId}")
  @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật sản phẩm với thông tin đã cho")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Sản phẩm được cập nhật thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tòn tại sản phẩm này hoặc đã bị xóa"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> updateProduct(Authentication authentication,@PathVariable String productId, @RequestBody CreateProductInfoDto dto) {
    try {
      return successApi("Cập nhật sản phẩm thành công", productService.updateProduct(getUsername(authentication),productId, dto));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("image")
  @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật sản phẩm với thông tin đã cho")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Sản phẩm được cập nhật thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tòn tại sản phẩm này hoặc đã bị xóa"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> updateProduct(Authentication authentication, @RequestParam String productId, @RequestParam String imageUrl) {
    try {
      productService.updateDefaultImage(getUsername(authentication), productId, imageUrl);
      return successApi("Cập nhật ảnh đại diện sản phẩm thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }
}
