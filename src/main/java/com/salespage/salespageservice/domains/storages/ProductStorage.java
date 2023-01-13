package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.utils.CacheKey;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

  public Page<Product> findAllProduct(Pageable pageable) {
    List<Product> cacheData = remoteCacheManager.getList(CacheKey.listProduct(pageable.getPageNumber()), Product.class);
    if (cacheData == null) {
      return productRepository.findAll(pageable);
    }
    log.debug("==========Get size product data: " + cacheData.size());
    return new PageImpl<>(cacheData, pageable, cacheData.size());
  }

  public void delete(String productId) {
    productRepository.deleteById(new ObjectId(productId));
  }

  public List<ProductTransaction> findAllProductById(String productId) {
    return productTransactionRepository.findAllProductTransactionByProduct_Id(new ObjectId(productId));
  }

  public void saveAll(List<ProductTransaction> productTransactions) {
    productTransactionRepository.saveAll(productTransactions);
  }
}
