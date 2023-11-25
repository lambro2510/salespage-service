package com.salespage.salespageservice.domains.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AggregationInfo {
  @JsonProperty("_id")
  String key;
}
