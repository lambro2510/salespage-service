package com.salespage.salespageservice.app.responses.ProductResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductTypeResponse {

  @Schema(description = "Loại sản phẩm")
  String productType;

  @Schema(description = "Tên loại sản phẩm")
  String typeName;
}
