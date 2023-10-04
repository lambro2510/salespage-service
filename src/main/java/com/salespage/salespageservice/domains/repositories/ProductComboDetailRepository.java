package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductCombo;
import com.salespage.salespageservice.domains.entities.ProductComboDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductComboDetailRepository extends MongoRepository<ProductComboDetail, ObjectId> {
}
