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
    remoteCacheManager.set(CacheKey.genRemoveKeyProductComboDetailProductIdIn(), key);
    List<ProductComboDetail> productCombo = remoteCacheManager.getList(key, ProductComboDetail.class);
    if (productCombo == null) {
      productCombo = productComboDetailRepository.findByProductIdIn(ids);
      remoteCacheManager.set(key, productCombo, RemoteCacheManager.FIVE_MIN);
    }
    return productCombo;
  }

  public void saveAll(List<ProductComboDetail> productComboDetails) {
    productComboDetailRepository.saveAll(productComboDetails);
    productComboDetails.forEach(this::removeCache);
  }

  public void removeCache (ProductComboDetail productComboDetail){
    remoteCacheManager.del(CacheKey.genProductComboDetailComboId(productComboDetail.getComboId()));
    remoteCacheManager.del(CacheKey.genProductComboDetailProductId(productComboDetail.getProductId()));
    remoteCacheManager.del(CacheKey.genProductComboDetailProductId(productComboDetail.getProductId()));
    remoteCacheManager.del(CacheKey.genRemoveKeyProductComboDetailProductIdIn());
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

  public ProductComboDetail findByComboIdAndProductIdNoCache(String comboId, String productId) {
    return productComboDetailRepository.findByComboIdAndProductId(comboId, productId);
  }

  public void save(ProductComboDetail productComboDetail) {
    productComboDetailRepository.save(productComboDetail);
    removeCache(productComboDetail);

  }

  public void delete(ProductComboDetail productComboDetail) {
    productComboDetailRepository.delete(productComboDetail);
    removeCache(productComboDetail);

  }
}
