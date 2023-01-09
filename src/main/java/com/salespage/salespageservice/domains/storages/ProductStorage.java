package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ProductStorage extends BaseStorage {
  public void save(Product product) {
    productRepository.save(product);
  }

  public Product findProductById(String productId) {
    return productRepository.findProductById(new ObjectId(productId));
  }

  public Page<Product> findAllProduct(Pageable pageable) {
    return productRepository.findAll(pageable);
  }
}
