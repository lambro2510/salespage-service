package com.salespage.salespageservice.app.dtos.CartDtos;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CartPaymentDto {

  String comboId;

  String note;

  List<ProductTransactionDto> transaction;
}
