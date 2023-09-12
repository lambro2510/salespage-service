package com.salespage.salespageservice.domains.entities.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SizeType {
  CENTIMES("cm"),
  MES("m");

  String unit;
}
