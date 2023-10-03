package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.Cart.CartDto;
import com.salespage.salespageservice.domains.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("api/v1/cart")
public class CartController extends BaseController{

  @Autowired
  CartService cartService;

  @GetMapping("")
  public ResponseEntity<?> getProductCart(Authentication authentication){
    try{
      return successApi(cartService.findCartByUsername(getUsername(authentication)));
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createCart(Authentication authentication, @RequestBody @Valid CartDto dto){
    try{
      cartService.createCart(getUsername(authentication), dto);
      return successApi("Thêm vào giỏ hàng thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @PutMapping("{id}")
  public ResponseEntity<?> updateCart(Authentication authentication, @PathVariable String id, @RequestParam Long quantity, @RequestParam(required = false) String voucherCodeId){
    try{
      cartService.updateCart(getUsername(authentication), id, quantity, voucherCodeId);
      return successApi("Cập nhật thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> deleteCart(Authentication authentication, @PathVariable String id){
    try{
      cartService.deleteCart(getUsername(authentication), id);
      return successApi("Xóa thành công");
    }catch (Exception ex){
      return errorApi(ex.getMessage());
    }
  }
}
