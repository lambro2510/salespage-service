package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productComboDtos.ComboDto;
import com.salespage.salespageservice.app.responses.ProductComboResponse.ProductComboResponse;
import com.salespage.salespageservice.domains.entities.ProductCombo;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductComboService extends BaseService{

  public void createProductCombo(String username, ComboDto dto){
    User user = userStorage.findByUsername(username);
    if(Objects.isNull(user)){
      throw new ResourceNotFoundException("Không tồn tại người dùng");
    }

    ProductCombo productCombo = modelMapper.toProductCombo(dto);
    productCombo.setCreatedBy(username);
    productComboStorage.save(productCombo);
  }

  public void updateProductCombo(String username, String comboId, ComboDto dto){
    User user = userStorage.findByUsername(username);
    if(Objects.isNull(user)){
      throw new ResourceNotFoundException("Không tồn tại người dùng");
    }
    
    ProductCombo productCombo = productComboStorage.findById(comboId);
    if(Objects.isNull(productCombo)){
      throw new ResourceNotFoundException("Không tồn tại combo này");
    }
    productCombo = modelMapper.toProductCombo(dto);
    productCombo.setCreatedBy(username);
    productComboStorage.save(productCombo);
  }

  public void deleteProductCombo(String username, String comboId){
    User user = userStorage.findByUsername(username);
    if(Objects.isNull(user)){
      throw new ResourceNotFoundException("Không tồn tại người dùng");
    }

    ProductCombo productCombo = productComboStorage.findById(comboId);
    if(Objects.isNull(productCombo)){
      throw new ResourceNotFoundException("Không tồn tại combo này");
    }
    productComboStorage.delete(productCombo);
  }

  public List<ProductComboResponse> getProductCombo(String username){
    List<ProductCombo> productCombos = productComboStorage.findByCreatedBy(username);
    return modelMapper.toListProductCombo(productCombos);
  }
}
