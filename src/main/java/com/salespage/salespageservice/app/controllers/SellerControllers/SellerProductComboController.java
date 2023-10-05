package com.salespage.salespageservice.app.controllers.SellerControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.dtos.productComboDtos.ComboDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.services.ProductComboService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Seller product combo", description = "Quản lý combo sản phẩm")
@RequestMapping("api/v1/seller/product-combo")
@RestController
@CrossOrigin
@SecurityRequirement(name = "bearerAuth")
public class SellerProductComboController extends BaseController {
  @Autowired
  ProductComboService productComboService;
  @GetMapping("{productId}")
  public ResponseEntity<BaseResponse> getProductCombo(Authentication authentication, @PathVariable String productId) {
    try {
      return successApi(productComboService.getProductCombo(getUsername(authentication)));
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<BaseResponse> createProductCombo(Authentication authentication, @RequestBody ComboDto dto) {
    try {
      productComboService.createProductCombo(getUsername(authentication), dto);
      return successApi("Tạo thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("{ComboId}")
  public ResponseEntity<BaseResponse> createProductCombo(Authentication authentication, @PathVariable String ComboId, @RequestBody ComboDto dto) {
    try {
      productComboService.updateProductCombo(getUsername(authentication), ComboId, dto);
      return successApi("Cập nhật thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("{ComboId}")
  public ResponseEntity<BaseResponse> deleteProductCombo(Authentication authentication, @PathVariable String ComboId) {
    try {
      productComboService.deleteProductCombo(getUsername(authentication), ComboId);
      return successApi("Xóa thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }
}
