package com.salespage.salespageservice.app.dtos.productDtos;

import lombok.Data;

@Data
public class ProductTypeDetailDto {
  String id;

  String productId;
  String typeName;

  String typeDetailName;

  String note;
}
