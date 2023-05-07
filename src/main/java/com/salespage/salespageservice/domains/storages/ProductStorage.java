package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Product;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class ProductStorage extends BaseStorage {
  public void save(Product product) {
    productRepository.save(product);
  }

  public Product findProductById(String productId) {
    return productRepository.findProductById(new ObjectId(productId));
  }

  public Page<Product> findAll(Query query, Pageable pageable) {
    return productRepository.findAll(query, pageable);

  }

  public void delete(String productId) {
    productRepository.deleteById(new ObjectId(productId));
  }

  public List<Product> findBySellerStoreId(String storeId) {
    return productRepository.findBySellerStoreId(storeId);
  }
}
