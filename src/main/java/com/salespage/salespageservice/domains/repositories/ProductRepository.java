package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {

  Product findProductById(ObjectId id);

  Page<Product> findAll(Query query, Pageable pageable);

  List<Product> findBySellerStoreIdsContaining(String storeId);

  List<Product> findTop11ByCategoryIdIn(List<String> productIds);

  List<Product> findTop10ByCategoryIdOrderByCreatedAtDesc(String typeName);

  List<Product> findByCategoryId(String categoryId);

  List<Product> findByIdIn(List<ObjectId> productIds);

  List<Product> findTopNByIdIn(Collection<ObjectId> id, @Param("limit") int limit);

  List<Product> findTop10ByIdIn(List<ObjectId> objectIds);
}
