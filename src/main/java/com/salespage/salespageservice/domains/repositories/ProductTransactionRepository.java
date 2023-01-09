package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTransactionRepository extends MongoRepository<ProductTransaction, ObjectId> {
    ProductTransaction findProductTransactionById(ObjectId objectId);
}
