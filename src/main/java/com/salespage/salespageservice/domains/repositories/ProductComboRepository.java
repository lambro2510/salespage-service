package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductCombo;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.entities.types.ActiveState;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductComboRepository extends MongoRepository<ProductCombo, ObjectId> {
  List<ProductCombo> findByCreatedBy(String username);

  ProductCombo findByIdAndState(ObjectId objectId, ActiveState activeState);

  List<ProductCombo> findByIdIn(List<ObjectId> objectIds);
}
