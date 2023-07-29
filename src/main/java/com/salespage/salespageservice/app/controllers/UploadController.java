package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RequestMapping("api/v1/upload")
@RestController
@CrossOrigin
public class UploadController extends BaseController{

  @Autowired private ProductService productService;

  @PostMapping("prodct")
  @Operation(summary = "Tải lên hình ảnh cho sản phẩm", description = "Tải lên một hoặc nhiều hình ảnh cho sản phẩm với ID đã cho")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hình ảnh tải lên thành công"),
      @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
      @ApiResponse(responseCode = "401", description = "Không được phép"),
      @ApiResponse(responseCode = "404", description = "Không tòn tại sản phẩm này hoặc đã bị xóa"),
      @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
  })
  public ResponseEntity<BaseResponse> uploadImages(@RequestParam @Valid @NotNull String productId,
                                                   @RequestParam @Valid @NotNull String token,
                                                   @RequestBody @Valid @NotNull List<MultipartFile> files){
    try {
      String username = getUserInfoFromToken(token);
      return successApi("Tải ảnh lên thành công", productService.uploadProductImage(username, productId, files));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }
}
