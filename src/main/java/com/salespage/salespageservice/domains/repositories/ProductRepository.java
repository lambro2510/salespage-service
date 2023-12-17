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

  List<Product> findTop12ByCategoryIdOrderByCreatedAtDesc(String typeName);

  List<Product> findByCategoryId(String categoryId);

  List<Product> findByIdIn(List<ObjectId> productIds);

  List<Product> findTopNByIdIn(Collection<ObjectId> id, @Param("limit") int limit);

  List<Product> findTop12ByIdIn(List<ObjectId> objectIds);

  List<Product> findByIdInAndCreatedBy(List<ObjectId> objectIds, String username);

  List<Product> findTop12ByIsHotOrderByUpdatedAtDesc(boolean b);

  List<Product> findTop12ByIsHotOrderByUpdatedAt(boolean b);

  List<Product> findTop12ByIdInAndIsHotOrderByUpdatedAt(List<ObjectId> objectIds);

  Page<Product> findByCreatedBy(String username, Pageable pageable);
}
