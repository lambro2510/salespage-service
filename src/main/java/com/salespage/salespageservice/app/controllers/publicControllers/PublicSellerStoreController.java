package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("v1/api/public/seller-store")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Thông tin của cửa hàng", description = "Thông tin của cửa hàng được hiển thị")
public class PublicSellerStoreController extends BaseController {

  @Autowired
  private SellerStoreService sellerStoreService;

  @GetMapping("")
  @Operation(summary = "Lấy thông tin toàn bộ các cửa hàng", description = "Lấy thông tin toàn bộ các cửa hàng")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<PageResponse<StoreDataResponse>> getAllStore(Pageable pageable) {
    return sellerStoreService.getAllStore(pageable);
  }

}
