package com.salespage.salespageservice.app.responses.transactionResponse;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.infor.ComboInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductTransactionResponse {

  List<ProductTransactionDetailResponse> details = new ArrayList<>();
  private String id;
  private String buyerUsername;
  private Double totalPrice;
  private String note;
  private ComboInfo comboInfo;
  private Long createdAt;

  public void partnerFromProductTransaction(ProductTransaction transaction) {
    id = transaction.getId().toHexString();
    buyerUsername = transaction.getBuyerUsername();
    totalPrice = transaction.getTotalPrice();
    note = transaction.getNote();
    comboInfo = transaction.getComboInfo();
    createdAt = transaction.getCreatedAt();
  }
}
