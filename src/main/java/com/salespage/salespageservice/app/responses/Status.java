package com.salespage.salespageservice.app.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
  VERY_GOOD(1),
  GOOD(2),
  NORMAL(3),
  POOR(4),
  VERY_POOR(5);

  public Integer rate;
}