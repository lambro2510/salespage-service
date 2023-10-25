package com.salespage.salespageservice.domains.entities.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
  VERY_SMALL("Rất nhỏ"),
  SMALL("Nhỏ"),
  BIG("Lớn"),
  LARGE("Rất lớn"),
  SUPPER_LARGE("Cực kỳ lớn");

  String name;
}
