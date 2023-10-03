package com.salespage.salespageservice.app.dtos.Cart;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartDto {
  @NotNull
  String productId;

  @NotNull
  @Min(1)
  Long quantity;

  String voucherId;
}
