package com.salespage.salespageservice.app.responses.ProductResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.domains.entities.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductResponse extends ProductDataResponse {

  @Schema(description = "URL ảnh sản phẩm")
  protected String imageUrl;

  @Schema(description = "Trạng thái sản phẩm hot")
  @JsonProperty("isHot")
  protected Boolean isHot = false;

  @Override
  public void assignFromProduct(Product product) {
    super.assignFromProduct(product);
    imageUrl = product.getDefaultImageUrl();
  }
}
