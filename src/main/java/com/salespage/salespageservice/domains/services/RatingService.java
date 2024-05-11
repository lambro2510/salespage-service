package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.rating.CreateRatingDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Rating;
import com.salespage.salespageservice.domains.entities.types.RatingType;
import com.salespage.salespageservice.domains.info.RatingInfo;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RatingService extends BaseService {

  private final ProductService productService;

  @Lazy
  public RatingService(ProductService productService) {
    this.productService = productService;
  }


  public PageResponse<Rating> getRatingOfProduct(String productId, Pageable pageable) {
    return PageResponse.createFrom(ratingStorage.findByRefIdAndRatingTypeOrderByUpdatedAt(productId, RatingType.PRODUCT, pageable));
  }

  public boolean createProductRating(String username, CreateRatingDto dto) {
    productService.updateRating(username, dto.getProductId(), dto.getPoint(), dto.getComment());
    return true;
  }
}
