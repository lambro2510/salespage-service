package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Rating;
import com.salespage.salespageservice.domains.entities.types.RatingType;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends MongoRepository<Rating, ObjectId> {
  Rating findByUsernameAndRefIdAndRatingType(String username, String productId, RatingType ratingType);

  Page<Rating> findByRefIdAndRatingTypeOrderByUpdatedAt(String refId, RatingType ratingType, Pageable pageable);
}
