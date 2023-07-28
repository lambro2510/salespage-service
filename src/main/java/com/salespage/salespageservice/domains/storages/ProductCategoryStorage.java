package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
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

  public ProductCategory findById(String id) {
    return productCategoryRepository.findById(new ObjectId(id)).orElseThrow(ResourceNotFoundException::new);
  }

  public void delete(ProductCategory productCategory) {
    productCategoryRepository.delete(productCategory);
  }
}
