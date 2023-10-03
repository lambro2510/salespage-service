package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.Cart.CartDto;
import com.salespage.salespageservice.app.responses.Cart.CartResponse;
import com.salespage.salespageservice.domains.entities.Cart;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CartService extends BaseService {

  @Autowired
  VoucherCodeService voucherCodeService;


  public void createCart(String username, CartDto dto) {
    Long countCartOfUser = cartStorage.countByUsername(username);
    if (countCartOfUser >= 10) {
      throw new BadRequestException("Vượt quá số lượng sản phẩm trong giỏ hàng.");
    }
    Product product = productStorage.findProductById(dto.getProductId());
    if (product == null) {
      throw new ResourceNotFoundException("Không tồn tại sản phẩm này");
    }
    VoucherInfo voucherInfo = voucherCodeService.getVoucherInfo(dto.getVoucherId());
    Cart cart = Cart.builder()
        .username(username)
        .productId(dto.getProductId())
        .productName(product.getProductName())
        .quantity(dto.getQuantity())
        .voucherInfo(voucherInfo)
        .voucherCodeId(dto.getVoucherId())
        .build();
    cartStorage.save(cart);
  }

  public List<CartResponse> findCartByUsername(String username) {
    List<Cart> carts = cartStorage.findByUsername(username);
    List<CartResponse> responses = new ArrayList<>();
    for (Cart cart : carts) {
      CartResponse response = new CartResponse();
      response.setCartId(cart.getId().toHexString());
      response.setProductId(cart.getProductId());

      response.setQuantity(cart.getQuantity());

      Product product = productStorage.findProductById(cart.getProductId());
      if (product == null) {
        response.setProductName(cart.getProductName());
        response.setProductNote("Sản phẩm không còn được bán");
        response.setCanPayment(false);
      } else {
        response.setProductName(product.getProductName());
        response.setProductNote("Còn " + product.getDetail().getQuantity() + " sản phẩm có sẵn");
        if (product.getDetail().getQuantity() <= cart.getQuantity()) {
          response.setProductNote(response.getProductNote() + "vui lòng điều chỉnh lại số lượng mua.");
          response.setCanPayment(false);
        }
      }

      VoucherInfo voucherInfo = voucherCodeService.getVoucherInfo(cart.getVoucherCodeId());
      if(voucherInfo == null){
        response.setVoucherNote("Chưa chọn mã giảm giá");
      }
      if(voucherInfo != null && product != null){
        Double totalPrice = voucherCodeService.getPriceWhenUseVoucher(product.getPrice() * cart.getQuantity(), voucherInfo.getDiscountType(), voucherInfo.getValue());
        response.setTotalPrice(totalPrice);
        response.setVoucherInfo(voucherInfo);
      }
      responses.add(response);
    }
    return responses;
  }

  public void updateCart(String username, String id, Long quantity, String voucherCodeId) {
    Cart cart = cartStorage.findById(id);
    if(!Objects.equals(cart.getUsername(), username)){
      throw new AuthorizationException();
    }
    VoucherInfo info = voucherCodeService.getVoucherInfo(voucherCodeId);
    Product product = productStorage.findProductById(cart.getProductId());
    if (product == null) {
      throw new ResourceNotFoundException("Sản phẩm không còn được bán");
    }

    if(product.getDetail().getQuantity() < quantity){
      throw new BadRequestException("Sản phẩm hiện không đủ số lượng");
    }

    cart.setQuantity(quantity);
    cart.setVoucherCodeId(voucherCodeId);
    cart.setVoucherInfo(info);
    cartStorage.save(cart);
  }

  public void deleteCart(String username, String id) {
    Cart cart = cartStorage.findById(id);
    if(cart == null){
      throw new ResourceNotFoundException("Không tồn tại");
    }

    if(!Objects.equals(cart.getUsername(), username)){
      throw new AuthorizationException();
    }

    cartStorage.delete(cart);
  }
}
