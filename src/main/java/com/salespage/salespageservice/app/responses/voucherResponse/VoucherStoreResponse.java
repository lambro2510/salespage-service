package com.salespage.salespageservice.app.responses.voucherResponse;

import com.salespage.salespageservice.domains.entities.status.VoucherStoreStatus;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import lombok.Data;

@Data
public class VoucherStoreResponse {
  private String voucherStoreName;

  private Long totalQuantity;

  private Long totalUsed;

  private VoucherStoreStatus voucherStoreStatus;

  private VoucherStoreType voucherStoreType;

  private String productId;

  private String productName;

  private Long price;
}
