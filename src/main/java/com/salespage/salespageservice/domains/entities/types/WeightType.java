package com.salespage.salespageservice.domains.entities.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WeightType {
  KILOGRAM("kg"),
  GRAM("g");

  String unit;
}
