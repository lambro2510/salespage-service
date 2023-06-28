package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.UserFavorite;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteRepository extends MongoRepository<UserFavorite, ObjectId> {

    UserFavorite findByUsernameAndRefIdAndFavoriteType(String username, String productId);

    List<UserFavorite> findByUsernameAndFavoriteType(String username, FavoriteType favoriteType);
}
