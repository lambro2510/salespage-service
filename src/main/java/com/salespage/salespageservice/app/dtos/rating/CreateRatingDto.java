package com.salespage.salespageservice.app.dtos.rating;

import lombok.Data;

@Data
public class CreateRatingDto {
  String productId;

  Float point = 0f;

  String comment;
}
