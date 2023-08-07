package com.salespage.salespageservice.app.responses.transactionResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TotalStatisticResponse {

  @Schema(description = "Tổng giá")
  Integer totalPrice = 0;

  @Schema(description = "Tổng số lượng")
  Integer quantity = 0;
}
