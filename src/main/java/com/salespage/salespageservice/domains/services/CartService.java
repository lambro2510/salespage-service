package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.Cart.CartDto;
import com.salespage.salespageservice.app.responses.Cart.CartByStoreResponse;
import com.salespage.salespageservice.app.responses.Cart.CartResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartService extends BaseService {

  @Autowired
  VoucherCodeService voucherCodeService;


  public void createCart(String username, CartDto dto) {
    Long countCartOfUser = cartStorage.countByUsername(username);
    if (countCartOfUser >= 10) {
      throw new BadRequestException("Vượt quá số lượng sản phẩm trong giỏ hàng.");
    }
    ProductDetail productDetail = productDetailStorage.findById(dto.getProductDetailId());
    if (productDetail == null) {
      throw new ResourceNotFoundException("Sản phẩm không còn được bán");
    }
    Product product = productStorage.findProductById(productDetail.getProductId());
    if (product == null) {
      throw new ResourceNotFoundException("Không tồn tại sản phẩm này");
    }
    SellerStore store = sellerStoreStorage.findById(dto.getStoreId());
    if (store == null) {
      throw new ResourceNotFoundException("Không tồn tại cửa hàng này");
    }
    VoucherInfo voucherInfo = voucherCodeService.getVoucherInfo(dto.getVoucherId(), username, true);
    Cart cart = Cart.builder()
        .username(username)
        .productDetailId(dto.getProductDetailId())
        .storeId(dto.getStoreId())
        .productName(product.getProductName())
        .quantity(dto.getQuantity())
        .voucherInfo(voucherInfo)
        .voucherCodeId(dto.getVoucherId())
        .build();
    cartStorage.save(cart);
  }

  public List<CartByStoreResponse> findCartByUsername(String username) {
    List<Cart> carts = cartStorage.findByUsername(username);
    List<CartResponse> responses = new ArrayList<>();
    for (Cart cart : carts) {
      CartResponse response = new CartResponse();
      response.setCartId(cart.getId().toHexString());
      response.setProductId(cart.getProductDetailId());
      response.setQuantity(cart.getQuantity());

      SellerStore store = sellerStoreStorage.findById(cart.getStoreId());
      if (store == null) {
        response.setStoreName("Cửa hàng đã bị xóa");
      }else{
        response.setStoreId(cart.getStoreId());
        response.setStoreName(store.getStoreName());
      }


      ProductDetail productDetail = productDetailStorage.findById(cart.getProductDetailId());
      if (productDetail == null) {
        response.setProductName(cart.getProductName());
        response.setProductNote("Sản phẩm không còn được bán");
        response.setCanPayment(false);
      } else {
        Product product = productStorage.findProductById(productDetail.getProductId());
        if(product == null){
          response.setProductName(cart.getProductName());
          response.setProductNote("Sản phẩm không còn được bán");
          response.setCanPayment(false);
        }else{
          response.setProductName(product.getProductName());
          response.setCategoryId(product.getCategoryId());
          response.setImageUrl(product.getDefaultImageUrl());
          ProductCategory productCategory = productCategoryStorage.findById(product.getCategoryId());
          if(Objects.nonNull(productCategory)){
            response.setCategoryName(productCategory.getCategoryName());
          }
        }

        response.setPrice(productDetail.getOriginPrice());
        response.setSellPrice(productDetail.getSellPrice());
        response.setDiscountPercent(productDetail.getDiscountPercent());
        response.setTotalPrice(productDetail.getSellPrice() * cart.getQuantity());

        response.setProductNote("Còn " + productDetail.getQuantity() + " sản phẩm có sẵn");
        if (productDetail.getQuantity() <= cart.getQuantity()) {
          response.setProductNote(response.getProductNote() + "vui lòng điều chỉnh lại số lượng mua.");
          response.setCanPayment(false);
        }
      }

      VoucherInfo voucherInfo = voucherCodeService.getVoucherInfo(cart.getVoucherCodeId(), username, false);
      if(voucherInfo == null){
        response.setVoucherNote("Chưa chọn mã giảm giá");
      }
      if(voucherInfo != null && productDetail != null){
        Double totalPrice = voucherCodeService.getPriceWhenUseVoucher(productDetail.getSellPrice() * cart.getQuantity(), voucherInfo.getDiscountType(), voucherInfo.getValue());
        response.setTotalPrice(totalPrice);
        response.setVoucherInfo(voucherInfo);
      }
      responses.add(response);
    }

    Map<String, List<CartResponse>> cartMap = responses.stream()
        .collect(Collectors.groupingBy(CartResponse::getStoreId));

    return cartMap.entrySet().stream()
        .map(entry -> {
          CartByStoreResponse cartByStoreResponse = new CartByStoreResponse();
          cartByStoreResponse.setStoreId(entry.getKey());
          cartByStoreResponse.setStoreName(entry.getValue().get(0).getStoreName());
          cartByStoreResponse.setCartResponses(entry.getValue());
          return cartByStoreResponse;
        })
        .collect(Collectors.toList());
  }

  public void updateCart(String username, String id, Long quantity, String voucherCodeId) {
    Cart cart = cartStorage.findById(id);
    if(!Objects.equals(cart.getUsername(), username)){
      throw new AuthorizationException();
    }
    VoucherInfo info = voucherCodeService.getVoucherInfo(voucherCodeId, username, true);
    ProductDetail productDetail = productDetailStorage.findById(cart.getProductDetailId());
    if (productDetail == null) {
      throw new ResourceNotFoundException("Sản phẩm không còn được bán");
    }

    if(productDetail.getQuantity() < quantity){
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
