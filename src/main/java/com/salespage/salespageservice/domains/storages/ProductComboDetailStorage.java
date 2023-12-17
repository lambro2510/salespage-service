package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductComboDetail;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.RemoteCacheManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductComboDetailStorage extends BaseStorage {
  public List<ProductComboDetail> findByProductIdIn(List<String> ids) {
    String key = CacheKey.genProductComboDetailProductIdIn(ids);
    List<ProductComboDetail> productCombo = remoteCacheManager.getList(key, ProductComboDetail.class);
    if (productCombo == null) {
      productCombo = productComboDetailRepository.findByProductIdIn(ids);
      remoteCacheManager.set(key, productCombo, RemoteCacheManager.FIVE_MIN);
    }
    return productCombo;
  }

  public void saveAll(List<ProductComboDetail> productComboDetails) {
    productComboDetailRepository.saveAll(productComboDetails);
    productComboDetails.forEach(k -> {
      remoteCacheManager.del(CacheKey.genProductComboDetailComboId(k.getComboId()));
      remoteCacheManager.del(CacheKey.genProductComboDetailProductId(k.getProductId()));
    });
  }

  public List<ProductComboDetail> findByComboId(String comboId) {
    String key = CacheKey.genProductComboDetailComboId(comboId);
    List<ProductComboDetail> productCombo = remoteCacheManager.getList(key, ProductComboDetail.class);
    if (productCombo == null) {
      productCombo = productComboDetailRepository.findByComboId(comboId);
      remoteCacheManager.set(key, productCombo, RemoteCacheManager.HOUR);
    }
    return productCombo;
  }

  public List<ProductComboDetail> findByComboIdNoCache(String comboId) {
    return productComboDetailRepository.findByComboId(comboId);

  }

  public List<ProductComboDetail> findByProductId(String productId) {
    String key = CacheKey.genProductComboDetailProductId(productId);
    List<ProductComboDetail> productCombo = remoteCacheManager.getList(key, ProductComboDetail.class);
    if (productCombo == null) {
      productCombo = productComboDetailRepository.findByProductId(productId);
      remoteCacheManager.set(key, productCombo, RemoteCacheManager.HOUR);
    }
    return productCombo;
  }

  public void deleteAll(List<ProductComboDetail> removeProductCombo) {
    productComboDetailRepository.deleteAll(removeProductCombo);
  }
}
