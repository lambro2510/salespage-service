package com.salespage.salespageservice.app.responses.storeResponse;

import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
import com.salespage.salespageservice.domains.entities.SellerStore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreDataResponse extends StoreInfoResponse {

  @Schema(description = "Danh sách thông tin sản phẩm cửa hàng")
  private List<ProductDataResponse> productDataResponses = new ArrayList<>();

  @Override
  public void assignFromSellerStore(SellerStore sellerStore) {
    super.assignFromSellerStore(sellerStore);
  }
}
