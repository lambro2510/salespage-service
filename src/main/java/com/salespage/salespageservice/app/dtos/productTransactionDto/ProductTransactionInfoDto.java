package com.salespage.salespageservice.app.dtos.productTransactionDto;

import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import lombok.Data;

@Data
public class ProductTransactionInfoDto {

  private String transactionId;

  private String address;

  private Long quantity;

  private String note;

  private ProductTransactionState state;
}
