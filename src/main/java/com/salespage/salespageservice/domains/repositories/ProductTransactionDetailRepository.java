package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductTransactionDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTransactionDetailRepository extends MongoRepository<ProductTransactionDetail, ObjectId> {
}
