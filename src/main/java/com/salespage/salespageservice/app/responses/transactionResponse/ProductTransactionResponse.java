package com.salespage.salespageservice.app.responses.transactionResponse;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class ProductTransactionResponse {
  private String transactionId;

  private String productId;

  private String productName;

  private Long pricePerProduct;

  private String sellerName;

  private String buyerName;

  private String storeName;

  private String note;

  private Long quantity;

  private Boolean isUseVoucher;

  private ProductTransactionState productTransactionState;

  private VoucherInfo voucherInfo;

  private LocalDateTime createdAt;

  public void partnerFromProductTransaction(ProductTransaction productTransaction){
    transactionId = productTransaction.getId().toHexString();
    productId = productTransaction.getProductId();
    productName = productTransaction.getProductName();
    pricePerProduct = productTransaction.getPricePerProduct().longValue();
    sellerName = productTransaction.getSellerUsername();
    buyerName = productTransaction.getBuyerUsername();
    storeName = productTransaction.getStoreName();
    quantity = productTransaction.getQuantity();
    isUseVoucher = productTransaction.getIsUseVoucher();
    productTransactionState = productTransaction.getState();
    voucherInfo = productTransaction.getVoucherInfo();
    note = productTransaction.getNote();
    Instant instant = Instant.ofEpochSecond(productTransaction.getCreatedAt());
    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
    createdAt = zonedDateTime.toLocalDateTime();
  }
}
