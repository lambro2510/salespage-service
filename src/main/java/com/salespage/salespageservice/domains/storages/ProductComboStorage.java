package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductCombo;
import com.salespage.salespageservice.domains.entities.types.ActiveState;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.Helper;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductComboStorage extends BaseStorage {
  public void save(ProductCombo productCombo) {
    productComboRepository.save(productCombo);
    remoteCacheManager.del(CacheKey.genProductComboByIdAndState(productCombo.getId().toHexString(), productCombo.getState()));
  }

  public ProductCombo findById(String comboId) {
    if (comboId == null) {
      return null;
    }
    return productComboRepository.findById(new ObjectId(comboId)).orElse(null);
  }

  public void delete(ProductCombo productCombo) {
    productComboRepository.delete(productCombo);
  }

  public List<ProductCombo> findByCreatedByAndStoreId(String username, String storeId) {
    return productComboRepository.findByCreatedByAndStoreId(username, storeId);
  }

  public ProductCombo findByIdAndState(String comboId, ActiveState activeState) {
    String key = CacheKey.genProductComboByIdAndState(comboId, activeState);
    ProductCombo productCombo = remoteCacheManager.get(key, ProductCombo.class);
    if (productCombo == null) {
      productCombo = productComboRepository.findByIdAndState(new ObjectId(comboId), activeState);
      remoteCacheManager.set(key, productCombo);
    }
    return productCombo;
  }

  public List<ProductCombo> findByIdIn(List<String> comboIds) {
    String key = CacheKey.genProductComboByIds(comboIds);
    List<ProductCombo> productCombos = remoteCacheManager.getList(key, ProductCombo.class);
    if (productCombos == null) {
      productCombos = productComboRepository.findByIdIn(Helper.convertListStringToListObjectId(comboIds));
      remoteCacheManager.set(key, productCombos);
    }
    return productCombos;
  }
}
