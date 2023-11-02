package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.bson.types.ObjectId;
import com.salespage.salespageservice.domains.utils.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCategoryStorage extends BaseStorage {
  public List<ProductCategory> findByCreatedBy(String username) {
    return productCategoryRepository.findByCreatedBy(username);
  }

  public ProductCategory findByCreatedByAndId(String username, String id) {
    return productCategoryRepository.findByCreatedByAndId(username, new ObjectId(id));
  }

  public void save(ProductCategory productCategory) {
    productCategoryRepository.save(productCategory);
    remoteCacheManager.get(CacheKey.genProductCategoryById(productCategory.getId().toHexString()));
  }

  public ProductCategory findById(String id) {
    String key = CacheKey.genProductCategoryById(id);
    ProductCategory productCategory = remoteCacheManager.get(key, ProductCategory.class);
    if(productCategory == null){
      productCategory = productCategoryRepository.findById(new ObjectId(id)).orElse(null);
      remoteCacheManager.set(key, productCategory);
    }
    return productCategory;
  }

  public void delete(ProductCategory productCategory) {
    productCategoryRepository.delete(productCategory);
  }

  public List<ProductCategory> findByCategoryName(String categoryName) {
    return productCategoryRepository.findByCategoryName(categoryName);
  }

  public List<ProductCategory> findByIdIn(List<String> ids) {
    String key = CacheKey.genProductCategoryByIdIn(ids);
    List<ProductCategory> productCategories = remoteCacheManager.getList(key, ProductCategory.class);
    if(productCategories == null){
      productCategories = productCategoryRepository.findByIdIn((Helper.convertListStringToListObjectId(ids)));
      remoteCacheManager.set(key, productCategories);
    }
    return productCategories;
  }
}
