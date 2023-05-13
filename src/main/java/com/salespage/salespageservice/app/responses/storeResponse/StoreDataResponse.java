package com.salespage.salespageservice.app.responses.storeResponse;

import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
import com.salespage.salespageservice.domains.entities.SellerStore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreDataResponse extends StoreInfoResponse {

  private List<ProductDataResponse> productDataResponses = new ArrayList<>();

  @Override
  public void assignFromSellerStore(SellerStore sellerStore) {
    super.assignFromSellerStore(sellerStore);
  }
}
