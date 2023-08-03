package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Rating;
import com.salespage.salespageservice.domains.entities.types.RatingType;
import org.springframework.stereotype.Component;

@Component
public class RatingStorage extends BaseStorage {
  public Rating findByUsernameAndRefIdAndAndRatingType(String username, String productId, RatingType ratingType) {
    return ratingRepository.findByUsernameAndRefIdAndAndRatingType(username, productId, ratingType);
  }

  public void save(Rating rating) {
    ratingRepository.save(rating);
  }
}
