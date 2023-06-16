package com.salespage.salespageservice.app.responses.BankResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BankListData {

  Long id;

  String name;

  String code;

  String bin;

  String shortName;

  String logo;

  Long transferSupported;

  Long lookupSupported;

  @JsonProperty("short_name")
  String shortname;

  Long support;

  Long isTransfer;

  @JsonProperty("swift_code")
  String swiftCode;
}
