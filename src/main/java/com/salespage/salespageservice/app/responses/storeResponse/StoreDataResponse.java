package com.salespage.salespageservice.app.responses.storeResponse;

import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
import com.salespage.salespageservice.domains.entities.SellerStore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreDataResponse {
  private String storeId;

  private String storeName;

  private String imageUrl;

  private Float totalRate;

  private Float avgRate;

  private List<ProductDataResponse> productDataResponses = new ArrayList<>();

  public void assignFromSellerStore(SellerStore sellerStore){
    storeId = sellerStore.getId().toHexString();
    imageUrl = sellerStore.getImageStoreUrl();
    storeName = sellerStore.getStoreName();
    totalRate = sellerStore.getRate().getTotalRate();
    avgRate = sellerStore.getRate().getAvgPoint();
  }

}
