package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Rating;
import com.salespage.salespageservice.domains.entities.types.RatingType;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class RatingStorage extends BaseStorage {
  public Rating findByUsernameAndRefIdAndAndRatingType(String username, String productId, RatingType ratingType) {
    return ratingRepository.findByUsernameAndRefIdAndAndRatingType(username, productId, ratingType);
  }

  public void save(Rating rating) {
    ratingRepository.save(rating);
  }

  public Page<Rating> findByRefIdAndRatingType(String productId, RatingType ratingType, Pageable pageable) {
    return ratingRepository.findByRefIdAndRatingType(productId, ratingType, pageable);
  }
}
