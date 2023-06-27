package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {

    Product findProductById(ObjectId id);

    Page<Product> findAll(Query query, Pageable pageable);

    List<Product> findBySellerStoreId(String storeId);

    List<Product> findByIdIn(List<String> productIds);

    List<Product> findTop10ByTypeOrderByCreatedAtDesc(String typeName);
}
