package com.salespage.salespageservice.app.responses.transactionResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DistinctProductResponse {

  @Schema(description = "ID sản phẩm")
  String productId;
}
