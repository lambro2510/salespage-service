package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductCategory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, ObjectId> {
  List<ProductCategory> findByCreatedBy(String username);

  ProductCategory findByCreatedByAndId(String username, ObjectId id);

  List<ProductCategory> findByCategoryNameLike(String categoryName);

  List<ProductCategory> findByIdIn(List<ObjectId> ids);
}
