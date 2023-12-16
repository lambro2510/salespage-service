package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductTransactionDetail;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTransactionDetailRepository extends MongoRepository<ProductTransactionDetail, ObjectId> {
  List<ProductTransactionDetail> findByTransactionIdIn(List<String> tranIds);

  long countDistinctUsernameByCreatedAtBetween(Long createdAt, Long createdAt2);

  long countByProductDetailIdAndCreatedAtBetween(String id, Long createdAt, Long createdAt2);

  Page<ProductTransactionDetail> findAll(Query query, Pageable pageable);
}
