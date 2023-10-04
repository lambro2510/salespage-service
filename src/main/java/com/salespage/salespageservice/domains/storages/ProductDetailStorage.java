package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductDetail;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDetailStorage extends BaseStorage{
  public List<ProductDetail> findByProductId(String productId) {
    return productDetailRepository.findByProductId(productId);
  }

  public ProductDetail findById(String detailId) {
    return productDetailRepository.findById(new ObjectId(detailId)).get();
  }

  public void save(ProductDetail productDetail) {
    productDetailRepository.save(productDetail);
  }

  public void delete(ProductDetail productDetail) {
    productDetailRepository.delete(productDetail);
  }
}
