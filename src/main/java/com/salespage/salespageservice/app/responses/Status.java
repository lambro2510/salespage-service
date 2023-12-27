package com.salespage.salespageservice.app.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
  VERY_GOOD(5),
  GOOD(4),
  NORMAL(3),
  POOR(2),
  VERY_POOR(1);

  public Integer rate;
}