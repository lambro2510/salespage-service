package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.CartDtos.CartDto;
import com.salespage.salespageservice.app.dtos.CartDtos.CartPaymentDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.responses.CartResponse.*;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
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

    List<VoucherStore> voucherStores = voucherStoreStorage.findByProductIdIn(products.stream().map(p -> p.getId().toHexString()).collect(Collectors.toList()));
    Map<String, VoucherStore> mapVoucherStore = voucherStores.stream()
        .collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));

    List<VoucherCode> voucherCodes = voucherCodeStorage.findByVoucherStoreIdInAndUserName(voucherStores.stream().map(k -> k.getId().toHexString()).collect(Collectors.toList()), username);
    Map<String, VoucherCode> mapVoucherCode = voucherCodes.stream()
        .collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));

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
        response.setProductNote("Sản phẩm " + cart.getProductName() + " không còn được bán");
        response.setCanPayment(false);
      } else {
        response.setProductDetailId(productDetail.getId().toHexString());
        response.setProductDetailName(productDetail.getType().getType());
        response.setProductDetailImageUrl(productDetail.getImageUrl());
        response.setLimit(productDetail.getQuantity());
        product = mapProduct.get(productDetail.getProductId());
        if (product == null) {
          response.setProductName(cart.getProductName());
          response.setProductNote("Sản phẩm " + cart.getProductName() + " không còn được bán");
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
      VoucherCode voucherCode = mapVoucherCode.get(cart.getVoucherCodeId());
      VoucherStore voucherStore = null;
      if (voucherCode != null) {
        voucherStore = mapVoucherStore.get(voucherCode.getVoucherStoreId());
      }
      VoucherInfo voucherInfo = voucherCodeService.getVoucherInfo(voucherCode, voucherStore, username, product, productDetail.getSellPrice(), false);
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

    Map<String, List<CartResponse>> cartMap = responses.stream().collect(Collectors.groupingBy(CartResponse::getStoreId));

    return cartMap.entrySet().stream()
        .map(entry -> {
          List<CartResponse> distinctProductIds = entry.getValue().stream()
              .distinct()
              .collect(Collectors.toList());


          double totalSellPrice = entry.getValue().stream()
              .mapToDouble(CartResponse::getSellPrice)
              .sum();
          List<CartResponse> cartResponses = entry.getValue();

          cartResponses.forEach(k -> {
            List<ProductComboDetail> comboOfProduct = mapComboDetail.get(k.getProductId());
            if (comboOfProduct == null) {
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
      if(dto.getTransaction().isEmpty()){
        continue;
      }
      List<Cart> deleteCard = new ArrayList<>();
      for (ProductTransactionDto transaction : dto.getTransaction()) {
        Cart cart = cartStorage.findById(transaction.getCartId());
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
        }else{
          info.setIsUse(false);
          info.setPriceAfter(productDetail.getSellPrice() * cart.getQuantity());
        }

        ProductTransactionDetail productTransactionDetail = productTransactionService.buildProductTransactionDetail(transactionId.toHexString(), productDetail, info, transaction.getAddress(), cart.getQuantity(), store, transaction.getNote(), username);
        transactionDetails.add(productTransactionDetail);
        deleteCard.add(cart);
      }
      List<ProductTransactionDetail> transactionDetails1 = transactionDetails.stream().distinct().collect(Collectors.toList());
      ComboInfo comboInfo = productComboService.getComboInfo(dto.getComboId(), transactionDetails1);
      userService.minusBalance(user, comboInfo.getSellPrice());
      ProductTransaction productTransaction = productTransactionService.buildProductTransaction(transactionId, user, dto.getNote(), comboInfo, transactionDetails1);
      productTransactionService.saveTransaction(productTransaction, transactionDetails);
      cartStorage.deleteAll(deleteCard);

    }
    userStorage.save(user);
  }

  public List<CartDataResponse> findCartByUsernameV1(String username) {
    List<CartDataResponse> responses = new ArrayList<>();
//    User user = userStorage.findByUsername(username);
//    if (user == null) {
//      throw new ResourceNotFoundException("Không tồn tài người dùng này");
//    }
//    List<Cart> carts = cartStorage.findByUsername(username);
//    List<String> storeIds = carts.stream().map(Cart::getStoreId).collect(Collectors.toList());
//    List<String> productDetailIds = carts.stream().map(Cart::getProductDetailId).collect(Collectors.toList());
//    List<SellerStore> sellerStores = sellerStoreStorage.findByIdIn(storeIds);
//    List<ProductDetail> productDetails = productDetailStorage.findByIdIn(productDetailIds);
//
//    List<String> productIds = productDetails.stream().map(ProductDetail::getProductId).collect(Collectors.toList());
//    List<Product> products = productStorage.findByIdIn(productIds);
//
//    Map<String, SellerStore> sellerStoreMap = sellerStores.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));
//    Map<String, ProductDetail> productDetailMap = productDetails.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));
//    Map<String, Product> productMap = products.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));
//    List<ProductComboDetail> comboDetails = productComboDetailStorage.findByProductIdIn(productIds);
//    List<String> comboIds = comboDetails.stream().map(ProductComboDetail::getComboId).collect(Collectors.toList());
//
//    List<ProductCombo> combos = productComboStorage.findByIdIn(comboIds);
//    Map<String, List<ProductComboDetail>> comboDetailsMap = comboDetails.stream().collect(Collectors.groupingBy(ProductComboDetail::getProductId));
//    Map<String, ProductCombo> combosMap = combos.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));
//
//    for(Cart cart : carts){
//      CartDataResponse response = new CartDataResponse();
//      response.setCartId(cart.getId().toHexString());
//      response.setProductDetailId(cart.getProductDetailId());
//      response.setProductQuantity(cart.getQuantity());
//      response.setProductName(cart.getProductName());
//      response.setStoreId(cart.getStoreId());
//      SellerStore sellerStore = sellerStoreMap.get(cart.getStoreId());
//      if(sellerStore != null){
//        response.setStoreName(sellerStore.getStoreName());
//      }
//
//      ProductDetail productDetail = productDetailMap.get(cart.getProductDetailId());
//      if(productDetail != null){
//        response.setQuantity(productDetail.getQuantity());
//        response.setSellPrice(productDetail.getSellPrice());
//        response.setType(productDetail.getType().getType());
//        Product product = productMap.get(productDetail.getId().toHexString());
//        if(product != null){
//          response.setImageUrl(product.getDefaultImageUrl());
//          response.setProductName(product.getProductName());
//          response.setProductId(product.getId().toHexString());
//          List<ProductComboDetail> details = comboDetailsMap.get(product.getId().toHexString());
//          List<ComboInfo> comboOfProduct = new ArrayList<>();
//          for(ProductComboDetail comboDetail : details){
//            ProductCombo combo = combosMap.get(comboDetail.getComboId());
//            if(combo != null){
//              ComboInfo comboInfo = new ComboInfo(combo, 0D, productDetail.getSellPrice());
//              comboOfProduct.add(comboInfo);
//            }
//          }
//          response.setComboInfos(comboOfProduct);
//        }
//      }
//      responses.add(response);
//    }
//
//    for(CartDataResponse response : responses){
//      List<ListProductCartResponse> cartResponses = new ArrayList<>();
//
//      List<ProductComboDetail> productComboDetails = comboDetailsMap.get(response.getProductId());
//      List<String> ids = productComboDetails.stream().map(ProductComboDetail::getProductId).collect(Collectors.toList());
//      List<Product> refProducts = productStorage.findByIdIn(ids);
//      Map<String, Product> refProductMap = refProducts.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));
//
//      ProductCombo combo = combosMap.get(productComboDetails.get(0).getComboId());
//      if(combo != null){
//        ListProductCartResponse listProductCartResponse = new ListProductCartResponse(combo);
//      for(ProductComboDetail comboDetail : productComboDetails){
//          Product product = refProductMap.get(comboDetail.getProductId());
//          if(product != null){
//            ProductInCartResponse productInCartResponse = new ProductInCartResponse(product);
//            listProductCartResponse.getProducts().add(productInCartResponse);
//          }
//        }
//      }
//      response.setRefCarts(cartResponses);
//    }
    return responses;
  }
}
