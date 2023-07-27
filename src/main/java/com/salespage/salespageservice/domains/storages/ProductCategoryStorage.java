package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductCategory;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCategoryStorage extends BaseStorage{
  public List<ProductCategory> findByCreatedBy(String username) {
    return productCategoryRepository.findByCreatedBy(username);
  }

  public ProductCategory findByCreatedByAndId(String username, String id) {
    return productCategoryRepository.findByCreatedByAndId(username, new ObjectId(id));
  }

  public void save(ProductCategory productCategory) {
    productCategoryRepository.save(productCategory);
  }
}
