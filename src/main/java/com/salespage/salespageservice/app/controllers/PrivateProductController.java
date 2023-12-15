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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Tag(name = "Product", description = "Thông tin sản phẩm được bán")
@CrossOrigin
@RestController
@RequestMapping("api/v1/product")
@SecurityRequirement(name = "bearerAuth")
public class PrivateProductController extends BaseController {

  @Autowired
  private ProductService productService;

  @PostMapping("rating")
  public ResponseEntity<BaseResponse> updateRating(Authentication authentication, @RequestParam String productId, @RequestParam Float point, @RequestParam String comment) {
    try {
      productService.updateRatingAsync(getUsername(authentication), productId, point, comment);
      return successApi("Đánh giá sản phẩm thành công");
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

}
