package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Rating;
import com.salespage.salespageservice.domains.entities.types.RatingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class RatingStorage extends BaseStorage {
  public Rating findByUsernameAndRefIdAndRatingType(String username, String productId, RatingType ratingType) {
    return ratingRepository.findByUsernameAndRefIdAndRatingType(username, productId, ratingType);
  }

  public void save(Rating rating) {
    ratingRepository.save(rating);
  }

  public Page<Rating> findByRefIdAndRatingTypeOrderByUpdatedAt(String productId, RatingType ratingType, Pageable pageable) {
    return ratingRepository.findByRefIdAndRatingTypeOrderByUpdatedAt(productId, ratingType, pageable);
  }
}
