package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductComboDetail;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductComboDetailStorage extends BaseStorage{
  public List<ProductComboDetail> findByProductIdIn(List<String> ids) {
    return productComboDetailRepository.findByProductIdIn(ids);
  }

  public void saveAll(List<ProductComboDetail> productComboDetails) {
    productComboDetailRepository.saveAll(productComboDetails);
  }
}
