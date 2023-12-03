package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends MongoRepository<ProductDetail, ObjectId> {
  List<ProductDetail> findByProductId(String productId);

  List<ProductDetail> findByIdIn(List<ObjectId> ids);

  List<ProductDetail> findByProductIdIn(List<String> ids);

  List<ProductDetail> findBySellPriceBetween(Double min, Double max);

  List<ProductDetail> findBySellPriceLessThanEqual(Double maxPrice);

  List<ProductDetail> findBySellPriceGreaterThanEqual(Double minPrice);
}
