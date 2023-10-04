package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductCombo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductComboStorage extends BaseStorage{
  public void save(ProductCombo productCombo) {
    productComboRepository.save(productCombo);
  }

  public ProductCombo findById(String comboId) {
    return productComboRepository.findById(new ObjectId(comboId)).get();
  }

  public void delete(ProductCombo productCombo) {
    productComboRepository.delete(productCombo);
  }

  public List<ProductCombo> findByCreatedBy(String username) {
    return productComboRepository.findByCreatedBy(username);
  }
}
