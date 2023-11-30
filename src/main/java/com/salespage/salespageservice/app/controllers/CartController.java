package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.CartDtos.CartDto;
import com.salespage.salespageservice.app.dtos.CartDtos.CartPaymentDto;
import com.salespage.salespageservice.domains.services.CartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/cart")
@Log4j2
public class CartController extends BaseController{

  @Autowired
  CartService cartService;

  @GetMapping("")
  public ResponseEntity<?> getProductCart(Authentication authentication){
    try{
      return successApi(cartService.findCartByUsername(getUsername(authentication)));
    }catch (Exception ex){
      return errorApi(ex);
    }
  }

  @GetMapping("all")
  public ResponseEntity<?> getAllProductCart(Authentication authentication, Pageable pageable){
    try{
      return successApi(cartService.findCartByUsername(getUsername(authentication), pageable));
    }catch (Exception ex){
      return errorApi(ex);
    }
  }

  @GetMapping("v1")
  public ResponseEntity<?> getProductCartV1(Authentication authentication){
    try{
      return successApi(cartService.findCartByUsernameV1(getUsername(authentication)));
    }catch (Exception ex){
      return errorApi(ex);
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createCart(Authentication authentication, @RequestBody @Valid CartDto dto){
    try{
      cartService.createCart(getUsername(authentication), dto);
      return successApi("Thêm vào giỏ hàng thành công");
    }catch (Exception ex){
      return errorApi(ex);
    }
  }

  @PostMapping("payment")
  public ResponseEntity<?> paymentProductInCart(Authentication authentication, @RequestBody @Valid List<CartPaymentDto> dto){
    try{
      cartService.paymentProductInCart(getUsername(authentication), dto);
      return successApi("Thanh toán thành công, đơn hàng đang được kiểm duyệt");
    }catch (Exception ex){
      log.error("=====>paymentProductInCart ", ex);
      return errorApi(ex);
    }
  }

  @PutMapping("{id}")
  public ResponseEntity<?> updateCart(Authentication authentication, @PathVariable String id, @RequestParam Long quantity, @RequestParam(required = false) String voucherCodeId){
    try{
      cartService.updateCart(getUsername(authentication), id, quantity, voucherCodeId);
      return successApi("Cập nhật thành công");
    }catch (Exception ex){
      return errorApi(ex);
    }
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> deleteCart(Authentication authentication, @PathVariable String id){
    try{
      cartService.deleteCart(getUsername(authentication), id);
      return successApi("Xóa thành công");
    }catch (Exception ex){
      return errorApi(ex);
    }
  }
}
