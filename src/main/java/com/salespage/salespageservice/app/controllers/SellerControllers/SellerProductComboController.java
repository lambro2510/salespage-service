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

import java.util.List;

@Tag(name = "Seller product combo", description = "Quản lý combo sản phẩm")
@RequestMapping("api/v1/seller/product-combo")
@RestController
@CrossOrigin
@SecurityRequirement(name = "bearerAuth")
public class SellerProductComboController extends BaseController {
  @Autowired
  ProductComboService productComboService;

  @GetMapping("")
  public ResponseEntity<BaseResponse> getProductCombo(Authentication authentication) {
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

  @PutMapping("{id}")
  public ResponseEntity<BaseResponse> updateProductCombo(Authentication authentication, @PathVariable String id, @RequestBody ComboDto dto) {
    try {
      productComboService.updateProductCombo(getUsername(authentication), id, dto);
      return successApi("Cập nhật thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("{id}")
  public ResponseEntity<BaseResponse> deleteProductCombo(Authentication authentication, @PathVariable String id) {
    try {
      productComboService.deleteProductCombo(getUsername(authentication), id);
      return successApi("Xóa thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("combo/{id}")
  public ResponseEntity<?> updateProductComboDetail(Authentication authentication, @PathVariable String id, @RequestBody List<String> ids) {
    try {
      productComboService.addProductToCombo(getUsername(authentication), id, ids);
      return successApi("Cập nhật thành công");
    } catch (Exception ex) {
      return errorApi(ex.getMessage());
    }
  }
}