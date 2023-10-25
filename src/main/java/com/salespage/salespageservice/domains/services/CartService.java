package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.CartDtos.CartDto;
import com.salespage.salespageservice.app.dtos.CartDtos.CartPaymentDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.responses.CartResponse.CartByStoreResponse;
import com.salespage.salespageservice.app.responses.CartResponse.CartResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.infor.ComboInfo;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartService extends BaseService {

  @Autowired
  VoucherCodeService voucherCodeService;

  @Autowired
  UserService userService;

  @Autowired
  ProductComboService productComboService;

  @Autowired
  ProductTransactionService productTransactionService;

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
    VoucherInfo voucherInfo = voucherCodeService.getVoucherInfo(dto.getVoucherId(), username, product, productDetail.getSellPrice(), true);
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
    List<ProductDetail> productDetails = productDetailStorage.findByIdIn(
        carts.stream()
            .map(Cart::getProductDetailId)
            .collect(Collectors.toList()));
    Map<String, ProductDetail> mapProductDetail = productDetails.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));

    List<Product> products = productStorage.findByIdIn(
        productDetails.stream()
            .map(ProductDetail::getProductId)
            .collect(Collectors.toList()));
    Map<String, Product> mapProduct = products.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));

    List<ProductCategory> productCategories = productCategoryStorage.findByIdIn(
        products.stream()
            .map(Product::getCategoryId)
            .collect(Collectors.toList()));
    Map<String, ProductCategory> mapProductCategory = productCategories.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));

    List<SellerStore> sellerStores = sellerStoreStorage.findByIdIn(
        carts.stream()
            .map(Cart::getStoreId)
            .collect(Collectors.toList()));
    Map<String, SellerStore> sellerStoresMap = sellerStores.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));

    List<ProductComboDetail> comboDetails = productComboDetailStorage.findByProductIdIn(
        products.stream()
            .map(k -> k.getId().toHexString())
            .collect(Collectors.toList())
    );

    Map<String, List<ProductComboDetail>> mapComboDetail = comboDetails.stream()
        .collect(Collectors.groupingBy(ProductComboDetail::getProductId));

    List<CartResponse> responses = new ArrayList<>();
    for (Cart cart : carts) {
      CartResponse response = new CartResponse();
      response.setCartId(cart.getId().toHexString());
      response.setProductId(cart.getProductDetailId());
      response.setQuantity(cart.getQuantity());

      SellerStore store = sellerStoresMap.get(cart.getStoreId());
      if (store == null) {
        response.setStoreName("Cửa hàng đã bị xóa");
      } else {
        response.setStoreId(cart.getStoreId());
        response.setStoreName(store.getStoreName());
      }

      Product product = null;
      ProductDetail productDetail = mapProductDetail.get(cart.getProductDetailId());
      if (productDetail == null) {
        productDetail = new ProductDetail();
        response.setProductName(cart.getProductName());
        response.setProductNote("Sản phẩm không còn được bán");
        response.setCanPayment(false);
      } else {
        response.setLimit(productDetail.getQuantity());
        product = mapProduct.get(productDetail.getProductId());
        if (product == null) {
          response.setProductName(cart.getProductName());
          response.setProductNote("Sản phẩm không còn được bán");
          response.setCanPayment(false);
        } else {
          response.setProductId(product.getId().toHexString());
          response.setProductName(product.getProductName());
          response.setCategoryId(product.getCategoryId());
          response.setImageUrl(product.getDefaultImageUrl());
          ProductCategory productCategory = mapProductCategory.get(product.getCategoryId());
          if (Objects.nonNull(productCategory)) {
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

      VoucherInfo voucherInfo = voucherCodeService.getVoucherInfo(cart.getVoucherCodeId(), username, product, productDetail.getSellPrice(),  false);
      if (voucherInfo == null) {
        response.setVoucherNote("Chưa chọn mã giảm giá");
      }
      if (voucherInfo != null) {
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
          List<String> distinctProductIds = entry.getValue().stream()
              .map(CartResponse::getProductId)
              .distinct()
              .collect(Collectors.toList());


          double totalSellPrice = entry.getValue().stream()
              .mapToDouble(CartResponse::getSellPrice)
              .sum();
          List<CartResponse> cartResponses = entry.getValue();

          cartResponses.forEach(k -> {
            List<ProductComboDetail> comboOfProduct = mapComboDetail.get(k.getProductId());
            if(comboOfProduct == null){
              comboOfProduct = new ArrayList<>();
            }
            k.setComboIds(comboOfProduct.stream().map(ProductComboDetail::getComboId).collect(Collectors.toList()));
          });

          CartByStoreResponse cartByStoreResponse = new CartByStoreResponse();
          cartByStoreResponse.setStoreId(entry.getKey());
          cartByStoreResponse.setStoreName(entry.getValue().get(0).getStoreName());
          cartByStoreResponse.setCartResponses(cartResponses);
          cartByStoreResponse.setCombos(productComboService.findAllComboByProductIds(distinctProductIds, totalSellPrice));
          cartByStoreResponse.setBestCombo();

          return cartByStoreResponse;
        })
        .collect(Collectors.toList());

  }

  public void updateCart(String username, String id, Long quantity, String voucherCodeId) {
    Cart cart = cartStorage.findById(id);
    if (!Objects.equals(cart.getUsername(), username)) {
      throw new AuthorizationException();
    }

    ProductDetail productDetail = productDetailStorage.findById(cart.getProductDetailId());
    if (productDetail == null) {
      throw new ResourceNotFoundException("Sản phẩm không còn được bán");
    }

    if (productDetail.getQuantity() < quantity) {
      throw new BadRequestException("Sản phẩm hiện không đủ số lượng");
    }

    Product product = productStorage.findProductById(productDetail.getProductId());
    if (product == null) {
      throw new ResourceNotFoundException("Sản phẩm không còn được bán");
    }

    VoucherInfo info = voucherCodeService.getVoucherInfo(voucherCodeId, username, product, productDetail.getSellPrice(), true);

    cart.setQuantity(quantity);
    cart.setVoucherCodeId(voucherCodeId);
    cart.setVoucherInfo(info);
    cartStorage.save(cart);
  }

  public void deleteCart(String username, String id) {
    Cart cart = cartStorage.findById(id);
    if (cart == null) {
      throw new ResourceNotFoundException("Không tồn tại");
    }

    if (!Objects.equals(cart.getUsername(), username)) {
      throw new AuthorizationException();
    }

    cartStorage.delete(cart);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public void paymentProductInCart(String username, List<CartPaymentDto> dtos) {
    User user = userStorage.findByUsername(username);
    if (user == null) {
      throw new ResourceNotFoundException("Không tồn tài người dùng này");
    }

    for (CartPaymentDto dto : dtos) {
      ObjectId transactionId = new ObjectId();

      List<ProductTransactionDetail> transactionDetails = new ArrayList<>();
      for (ProductTransactionDto transaction : dto.getTransaction()) {
        Cart cart = cartStorage.findById(transaction.getProductDetailId());
        if (cart == null) {
          throw new ResourceNotFoundException("Không thấy sản phẩm trong giỏ hàng");
        }
        ProductDetail productDetail = productDetailStorage.findById(transaction.getProductDetailId());
        if (productDetail == null) {
          throw new ResourceNotFoundException("Vật phẩm không còn được bán");
        }
        Product product = productStorage.findProductById(productDetail.getProductId());
        if (product == null) {
          throw new ResourceNotFoundException("Vật phẩm không còn được bán");
        }
        SellerStore store = sellerStoreStorage.findById(transaction.getStoreId());
        if (store == null) {
          throw new ResourceNotFoundException("Cửa hàng không còn hoạt động");
        }
        VoucherInfo info = new VoucherInfo();
        if (StringUtils.isNotBlank(transaction.getVoucherCodeId())) {
          info = voucherCodeService.getVoucherInfoAndUse(transaction.getVoucherCodeId(), username, product, productDetail.getSellPrice());
        }
        ProductTransactionDetail productTransactionDetail = productTransactionService.buildProductTransactionDetail(productDetail, info, transaction.getAddress(), cart.getQuantity(), store, transaction.getNote());
        transactionDetails.add(productTransactionDetail);
      }
      long distinctProduct = transactionDetails.stream().map(ProductTransactionDetail::getProductDetailId).distinct().count();
      Double totalPrice = transactionDetails.stream().mapToDouble(ProductTransactionDetail::getTotalPrice).sum();
      ComboInfo comboInfo = productComboService.getComboInfo(dto.getComboId(), totalPrice, distinctProduct);
      ProductTransaction productTransaction = productTransactionService.buildProductTransaction(transactionId, username, dto.getNote(), comboInfo, transactionDetails);
      productTransactionService.saveTransaction(productTransaction, transactionDetails);
      userService.minusBalance(user, totalPrice);
    }

  }
}
