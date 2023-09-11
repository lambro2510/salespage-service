package com.salespage.salespageservice.domains.entities.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VehicleType {
  CAR("car"),
  BIKE("bike");

  final String value;
}
