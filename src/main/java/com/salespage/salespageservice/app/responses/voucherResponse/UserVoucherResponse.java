package com.salespage.salespageservice.app.responses.voucherResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVoucherResponse {

  String voucherStoreName;

  String voucherCode;
}
