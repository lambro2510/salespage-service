package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.CartDtos.CartDto;
import com.salespage.salespageservice.app.dtos.productComboDtos.ComboDto;
import com.salespage.salespageservice.domains.services.ProductComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("api/v1/product-combo")
public class ProductComboController extends BaseController{

  @Autowired
  ProductComboService productComboService;

  @GetMapping("")
  public ResponseEntity<?> getProductCombo(Authentication authentication){
    try{
      return successApi(productComboService.getProductCombo(getUsername(authentication)));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createProductCombo(Authentication authentication, @RequestBody @Valid ComboDto dto){
    try{
      productComboService.createProductCombo(getUsername(authentication), dto);
      return successApi("Thêm vào giỏ hàng thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("{id}")
  public ResponseEntity<?> updateProductCombo(Authentication authentication, @PathVariable String id,@RequestBody @Valid ComboDto dto){
    try{
      productComboService.updateProductCombo(getUsername(authentication), id, dto);
      return successApi("Cập nhật thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> deleteProductCombo(Authentication authentication, @PathVariable String id){
    try{
      productComboService.deleteProductCombo(getUsername(authentication), id);
      return successApi("Xóa thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
