package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productComboDtos.ComboDto;
import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboDetailResponse;
import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductCombo;
import com.salespage.salespageservice.domains.entities.ProductComboDetail;
import com.salespage.salespageservice.domains.entities.User;
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

  public List<ProductComboDetailResponse> findAllComboByProductIds(List<String> ids, Double totalPrice) {
    List<ProductComboDetailResponse> responses = new ArrayList<>();
    List<ProductComboDetail> productComboDetails = productComboDetailStorage.findByProductIdIn(ids);
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

        sellPrice = checkDiscountPrice(productCombo, sellPrice, ids.size());
        if(sellPrice == 0D){
          response.setCanUse(false);
        }
      }

      List<ProductComboDetail> comboDetails = productComboDetailStorage.findByComboId(productCombo.getId().toHexString());
      response.setProductDetailIds(comboDetails.stream().map(ProductComboDetail::getProductId).collect(Collectors.toList()));
      modelMapper.mapToProductComboDetailResponse(productCombo, response);
      responses.add(response);
    }
    return responses;
  }

  public double checkDiscountPrice(ProductCombo combo, Double price, long item) {
    if (item < combo.getQuantityToUse()) {
      return 0D;
    } else {
      if (price > combo.getMaxDiscount()) {
        price = combo.getMaxDiscount();
      }
    }
    return price;
  }

  public ComboInfo getComboInfo(String comboId, Double totalPrice, long item) {
    ProductCombo productCombo = productComboStorage.findById(comboId);
    if (productCombo == null) {
      throw new ResourceNotFoundException("Không tồn tại combo này");
    }
    Double priceAfterUse = checkDiscountPrice(productCombo, totalPrice, item);

    return new ComboInfo(productCombo, totalPrice - priceAfterUse, priceAfterUse);
  }

  public void addProductToCombo(String username,String comboId, List<String> productIds){
    List<Product> products = productStorage.findByIdInAndCreatedBy(productIds, username);
    List<ProductComboDetail> productComboDetails = new ArrayList<>();
    for(Product product : products){
      ProductComboDetail productComboDetail = new ProductComboDetail();
      productComboDetail.setComboId(comboId);
      productComboDetail.setProductId(product.getId().toHexString());
      productComboDetails.add(productComboDetail);
    }
    productComboDetailStorage.saveAll(productComboDetails);
  }
}
