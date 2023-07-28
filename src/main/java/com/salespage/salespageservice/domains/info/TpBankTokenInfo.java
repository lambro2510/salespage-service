package com.salespage.salespageservice.domains.info;

import lombok.Data;

@Data
public class TpBankTokenInfo {
  String access_token;

  String expires_in;

  String token_type;
}
