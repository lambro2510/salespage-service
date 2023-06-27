package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTransactionRepository extends MongoRepository<ProductTransaction, ObjectId> {
    ProductTransaction findProductTransactionById(ObjectId id);

    Page<ProductTransaction> findAll(Query query, Pageable pageable);

    List<ProductTransaction> findAllProductTransactionByProductId(ObjectId productId);

    @org.springframework.data.mongodb.repository.Query(value = "")
    long countByProductId(ObjectId productId);
}
