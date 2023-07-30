package com.salespage.salespageservice.app.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingResponse {
  private Long ratingPoint;

  private Float totalRate;

  private Float abgPoint;

}
