package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.UserFavorite;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteRepository extends MongoRepository<UserFavorite, ObjectId> {

    UserFavorite findByUsernameAndRefIdAndFavoriteType(String username, String productId);
}
