package com.salespage.salespageservice.app.dtos.productTransactionDto;

import com.salespage.salespageservice.domains.entities.Product;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductTransactionDto extends ProductTransactionInfoDto {

  @NotNull
  private String productId ;

  private String voucherCode;
}
