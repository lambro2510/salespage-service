package com.salespage.salespageservice.app.controllers.SellerControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import com.salespage.salespageservice.domains.services.ProductTransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Seller product transaction", description = "Quản lý lịch sử bán hàng")
@CrossOrigin
@RestController
@RequestMapping("api/v1/seller/product-transaction")
@SecurityRequirement(name = "bearerAuth")
public class SellerProductTransactionController extends BaseController {

  @Autowired
  private ProductTransactionService productTransactionService;

  @GetMapping("")
  public ResponseEntity<?> getAllTransaction(
      Authentication authentication,
      @RequestParam(required = false) String productId,
      @RequestParam(required = false) String productName,
      @RequestParam(required = false) String buyerName,
      @RequestParam(required = false) String sellerStoreId,
      @RequestParam(required = false) String sellerStoreName,
      @RequestParam(required = false) ProductTransactionState state,
      @RequestParam(required = false) Long lte,
      @RequestParam(required = false) Long gte,
      Pageable pageable
  ) {
    try {
      return successApi(productTransactionService.getAllTransactionByUser(getUsername(authentication), productId,productName,buyerName,sellerStoreId,sellerStoreName, state, lte, gte, pageable));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("{id}")
  public ResponseEntity<?> acceptTransactionByStore(
      Authentication authentication,
      @PathVariable String id
  ) {
    try {
//      productTransactionService.acceptTransactionByStore(getUsername(authentication), id);
      return successApi("Đơn hàng đã được bạn nhận thành công.");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }


}
