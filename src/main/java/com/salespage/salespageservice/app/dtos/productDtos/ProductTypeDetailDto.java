package com.salespage.salespageservice.app.dtos.productDtos;

import lombok.Data;

@Data
public class ProductTypeDetailDto {

  String productId;
  
  String typeName;

  String typeDetailName;

  String note;
}