package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.FavoriteProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteProductRepository extends MongoRepository<FavoriteProduct, ObjectId> {
  FavoriteProduct findByUsernameAndProductId(String username, String productId);
}
