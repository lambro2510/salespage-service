package com.salespage.salespageservice.app.dtos.productTransactionDto;

import com.salespage.salespageservice.domains.entities.Product;
import lombok.Data;

@Data
public class ProductTransactionDto extends ProductTransactionInfoDto {

  private String productId ;

  private String voucherCode;
}
