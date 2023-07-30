package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingStorage extends BaseStorage{
  public Rating findByUsernameAndProductId(String username, String productId) {
    return ratingRepository.findByUsernameAndProductId(username, productId);
  }

  public void save(Rating rating) {
    ratingRepository.save(rating);
  }
}
