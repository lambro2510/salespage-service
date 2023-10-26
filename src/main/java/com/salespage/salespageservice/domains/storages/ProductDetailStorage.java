package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.bson.types.ObjectId;
import com.salespage.salespageservice.domains.utils.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDetailStorage extends BaseStorage{
  public List<ProductDetail> findByProductId(String productId) {
    String key = CacheKey.genProductDetailByProductId(productId);
    List<ProductDetail> productDetails = remoteCacheManager.getList(key, ProductDetail.class);
    if(productDetails == null){
      productDetails = productDetailRepository.findByProductId(productId);
      remoteCacheManager.set(key, productDetails);
    }
    return productDetails;
  }

  public ProductDetail findById(String detailId) {
    String key = CacheKey.genProductDetail(detailId);
    ProductDetail productDetail = remoteCacheManager.get(key, ProductDetail.class);
    if(productDetail == null){
      productDetail = productDetailRepository.findById(new ObjectId(detailId)).get();
      remoteCacheManager.set(key, productDetail);
    }
    return productDetail;
  }

  public void save(ProductDetail productDetail) {
    productDetailRepository.save(productDetail);
    remoteCacheManager.del(CacheKey.genProductDetail(productDetail.getId().toHexString()));
    remoteCacheManager.del(CacheKey.genProductDetailByProductId(productDetail.getProductId()));
  }

  public void delete(ProductDetail productDetail) {
    remoteCacheManager.del(CacheKey.genProductDetail(productDetail.getId().toHexString()));
    productDetailRepository.delete(productDetail);
  }

  public List<ProductDetail> findByIdIn(List<String> ids) {
    String key = CacheKey.genProductDetailByIdIn(ids);
    List<ProductDetail> productDetails = remoteCacheManager.getList(key, ProductDetail.class);
    if(productDetails == null){
      productDetails = productDetailRepository.findByIdIn((Helper.convertListStringToListObjectId(ids)));
      remoteCacheManager.set(key, productDetails);
    }
    return productDetails;
  }
}
