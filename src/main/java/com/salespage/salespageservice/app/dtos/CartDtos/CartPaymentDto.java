package com.salespage.salespageservice.app.dtos.CartDtos;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import lombok.Data;

import java.util.List;

@Data
public class CartPaymentDto {

  String comboId;

  String note;

  List<ProductTransactionDto> transaction;
}
