package com.salespage.salespageservice.app.responses.transactionResponse;

import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import lombok.Data;

@Data
public class ProductTransactionResponse {
  private String transactionId;

  private String productId;

  private String productname;

  private Long pricePerProduct;

  private Long quantity;

  private Boolean isUseVoucher;

  private ProductTransactionState productTransactionState;

  private VoucherInfo voucherInfo;

  public void partnerFromProduct
}
