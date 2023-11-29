package com.salespage.salespageservice.app.responses.transactionResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.ProductTransactionDetail;
import com.salespage.salespageservice.domains.entities.infor.ComboInfo;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductTransactionResponse {

  private String id;

  private String buyerUsername;

  private Double totalPrice;

  private String note;

  private ComboInfo comboInfo;

  private Long createdAt;

  List<ProductTransactionDetailResponse> details = new ArrayList<>();

  public void partnerFromProductTransaction(ProductTransaction transaction){
    id = transaction.getId().toHexString();
    buyerUsername = transaction.getBuyerUsername();
    totalPrice = transaction.getTotalPrice();
    note = transaction.getNote();
    comboInfo = transaction.getComboInfo();
    createdAt = transaction.getCreatedAt();
  }
}
