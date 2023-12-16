package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productComboDtos.ComboDto;
import com.salespage.salespageservice.app.responses.CartResponse.CartResponse;
import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboDetailResponse;
import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.infor.ComboInfo;
import com.salespage.salespageservice.domains.entities.types.ActiveState;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductComboService extends BaseService {

  public void createProductCombo(String username, ComboDto dto) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) {
      throw new ResourceNotFoundException("Không tồn tại người dùng");
    }

    ProductCombo productCombo = modelMapper.toProductCombo(dto);
    productCombo.setCreatedBy(username);
    productComboStorage.save(productCombo);
  }

  public void updateProductCombo(String username, String comboId, ComboDto dto) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) {
      throw new ResourceNotFoundException("Không tồn tại người dùng");
    }

    ProductCombo productCombo = productComboStorage.findById(comboId);
    if (Objects.isNull(productCombo)) {
      throw new ResourceNotFoundException("Không tồn tại combo này");
    }
    modelMapper.mapToProductCombo(dto, productCombo);
    productCombo.setCreatedBy(username);
    productComboStorage.save(productCombo);
  }

  public void deleteProductCombo(String username, String comboId) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) {
      throw new ResourceNotFoundException("Không tồn tại người dùng");
    }

    ProductCombo productCombo = productComboStorage.findById(comboId);
    if (Objects.isNull(productCombo)) {
      throw new ResourceNotFoundException("Không tồn tại combo này");
    }
    productComboStorage.delete(productCombo);
  }

  public List<ProductComboResponse> getProductCombo(String username) {
    List<ProductCombo> productCombos = productComboStorage.findByCreatedBy(username);
    return modelMapper.toListProductCombo(productCombos);
  }

  public List<ProductComboDetailResponse> findAllComboByProductIds(List<CartResponse> cartResponses, Double totalPrice) {
    List<ProductComboDetailResponse> responses = new ArrayList<>();
    List<String> productIds = cartResponses.stream().map(CartResponse::getProductId).collect(Collectors.toList());
    List<ProductComboDetail> productComboDetails = productComboDetailStorage.findByProductIdIn(productIds);

    Map<String, List<ProductComboDetail>> groupedByProductComboId = productComboDetails.stream()
        .collect(Collectors.groupingBy(ProductComboDetail::getComboId));
    for (String comboId : groupedByProductComboId.keySet()) {
      ProductComboDetailResponse response = new ProductComboDetailResponse();

      ProductCombo productCombo = productComboStorage.findByIdAndState(comboId, ActiveState.ACTIVE);
      if (Objects.isNull(productCombo)) {
        response.setCanUse(false);
      } else {
        double sellPrice = 0D;
        if (productCombo.getType().equals(DiscountType.PERCENT)) {
          sellPrice = totalPrice - totalPrice * productCombo.getValue() / 100;
        } else if (productCombo.getType().equals(DiscountType.TOTAL)) {
          sellPrice = totalPrice - productCombo.getValue();
        }

        sellPrice = checkDiscountPriceInCart(productCombo, sellPrice, cartResponses);
        if (sellPrice == 0D) {
          response.setCanUse(false);
        }
      }

      List<ProductComboDetail> comboDetails = productComboDetailStorage.findByComboId(productCombo.getId().toHexString());
      List<Product> products = productStorage.findByIdIn(comboDetails.stream().map(ProductComboDetail::getProductId).collect(Collectors.toList()));
      response.setProducts(modelMapper.toListProductInfoResponse(products));

      modelMapper.mapToProductComboDetailResponse(productCombo, response);
      responses.add(response);
    }
    return responses;
  }


  public List<String> findComboIdOfProduct(String productId) {
    List<ProductComboDetail> productComboDetails = productComboDetailStorage.findByProductId(productId);
    Map<String, List<ProductComboDetail>> groupedByProductComboId = productComboDetails.stream()
        .collect(Collectors.groupingBy(ProductComboDetail::getComboId));
    return new ArrayList<>(groupedByProductComboId.keySet());
  }

  public double checkDiscountPriceInTran(ProductCombo combo, double price, List<ProductTransactionDetail> products) {
    double sellPrice = 0;
    double notSalePrice = 0;
    List<ProductComboDetail> comboDetails = productComboDetailStorage.findByComboId(combo.getId().toHexString());
    List<String> productInCombo = comboDetails.stream().map(ProductComboDetail::getProductId).collect(Collectors.toList());
    for (ProductTransactionDetail transactionDetail : products) {
      if (productInCombo.contains(transactionDetail.getProductDetail().getProductId())) {
        sellPrice += transactionDetail.getTotalPrice();
      } else {
        notSalePrice += transactionDetail.getTotalPrice();
      }
    }
    if (combo.getType().equals(DiscountType.PERCENT)) {
      sellPrice = sellPrice - sellPrice * (combo.getValue() / 100);
    } else if (combo.getType().equals(DiscountType.TOTAL)) {
      sellPrice = sellPrice - combo.getValue();
    }
    if (products.size() < combo.getQuantityToUse()) {
      return 0D;
    } else {
      if (price - sellPrice > combo.getMaxDiscount()) {
        sellPrice = combo.getMaxDiscount();
      }
      if (sellPrice < 0) {
        sellPrice = 0;
      }
    }
    return sellPrice + notSalePrice;
  }

  public double checkDiscountPriceInCart(ProductCombo combo, double price, List<CartResponse> carts) {
    double sellPrice = 0;
    List<ProductComboDetail> comboDetails = productComboDetailStorage.findByComboId(combo.getId().toHexString());
    List<String> productInCombo = comboDetails.stream().map(ProductComboDetail::getProductId).collect(Collectors.toList());
    for (CartResponse cartResponse : carts) {
      if (productInCombo.contains(cartResponse.getProductId())) {
        sellPrice += cartResponse.getTotalPrice();
      }
    }
    if (combo.getType().equals(DiscountType.PERCENT)) {
      sellPrice = sellPrice - sellPrice * (combo.getValue() / 100);
    } else if (combo.getType().equals(DiscountType.TOTAL)) {
      sellPrice = sellPrice - combo.getValue();
    }
    if (carts.size() < combo.getQuantityToUse()) {
      return 0D;
    } else {
      if (price - sellPrice > combo.getMaxDiscount()) {
        sellPrice = combo.getMaxDiscount();
      }
      if (sellPrice < 0) {
        sellPrice = 0;
      }
    }
    return sellPrice;
  }

  public ComboInfo getComboInfo(String comboId, List<ProductTransactionDetail> transactions) {
    double totalPrice = transactions.stream().mapToDouble(ProductTransactionDetail::getTotalPrice).sum();
    ProductCombo productCombo = productComboStorage.findById(comboId);
    if (productCombo == null) {
      return new ComboInfo(null, 0D, totalPrice);
    }
    double priceAfterUse = checkDiscountPriceInTran(productCombo, totalPrice, transactions);

    return new ComboInfo(productCombo, totalPrice - priceAfterUse, priceAfterUse);
  }

  public void addProductToCombo(String username, String comboId, List<String> productIds) {
    List<Product> products = productStorage.findByIdInAndCreatedBy(productIds, username);
    List<ProductComboDetail> productComboDetails = new ArrayList<>();
    for (Product product : products) {
      ProductComboDetail productComboDetail = new ProductComboDetail();
      productComboDetail.setComboId(comboId);
      productComboDetail.setProductId(product.getId().toHexString());
      productComboDetails.add(productComboDetail);
    }
    productComboDetailStorage.saveAll(productComboDetails);
  }
}
