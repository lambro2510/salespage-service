package com.salespage.salespageservice.app.responses.voucherResponse;

import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListVoucherCodeResponse {
  List<VoucherCodeResponse> voucherCodes = new ArrayList<>();
  private String voucherStoreId;
  private String voucherStoreName;
  private VoucherStoreStatus voucherStoreStatus;
}
