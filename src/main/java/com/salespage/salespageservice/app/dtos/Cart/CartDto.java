package com.salespage.salespageservice.app.dtos.Cart;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartDto {
  @NotNull
  String productDetailId;

  @NotNull
  @Min(1)
  Long quantity;

  @NotNull
  String storeId;

  String voucherId;
}
