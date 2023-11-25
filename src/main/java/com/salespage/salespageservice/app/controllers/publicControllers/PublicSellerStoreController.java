package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
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
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/public/seller-store")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Store", description = "Thông tin của cửa hàng được hiển thị")
public class PublicSellerStoreController extends BaseController {

  @Autowired
  private SellerStoreService sellerStoreService;

  @GetMapping("")
  @Operation(summary = "Lấy thông tin toàn bộ các cửa hàng", description = "Lấy thông tin toàn bộ các cửa hàng")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<BaseResponse> getAllStore(@RequestParam(required = false) String storeId,
                                                  @RequestParam(required = false) String storeName,
                                                  Pageable pageable) {
    try {
      return successApi(sellerStoreService.getAllStore(storeId, storeName, pageable));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

  @GetMapping("detail")
  @Operation(summary = "Lấy thông tin chi tiết", description = "Lấy thông tin chi tiết")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Thành công"),
      @ApiResponse(responseCode = "500", description = "Lỗi hệ thông")
  })
  public ResponseEntity<BaseResponse> getStoreDetail(@RequestParam String storeId) {
    try {
      return successApi(sellerStoreService.getStoreDetail(storeId));
    } catch (Exception ex) {
      return errorApi(ex);
    }
  }

}
