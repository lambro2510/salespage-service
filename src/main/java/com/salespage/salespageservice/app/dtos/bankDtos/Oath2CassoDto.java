package com.salespage.salespageservice.app.dtos.bankDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Oath2CassoDto {

  @JsonProperty("client_id")
  String clientId;

  String scope;

  @JsonProperty("redirect_uri")
  String redirectUri;

  @JsonProperty("response_type")
  String responseType;

  String state;
}
