package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductComboDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductComboDetailRepository extends MongoRepository<ProductComboDetail, ObjectId> {
  List<ProductComboDetail> findByProductIdIn(List<String> ids);

  List<ProductComboDetail> findByComboId(String comboId);

  List<ProductComboDetail> findByProductId(String productId);
}
