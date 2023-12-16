package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Rating;
import com.salespage.salespageservice.domains.entities.types.RatingType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RatingService extends BaseService{
  public PageResponse<Rating> getRatingOfProduct(String productId, Pageable pageable) {
    return PageResponse.createFrom(ratingStorage.findByRefIdAndRatingTypeOrderByUpdatedAt(productId, RatingType.PRODUCT, pageable));
  }
}
