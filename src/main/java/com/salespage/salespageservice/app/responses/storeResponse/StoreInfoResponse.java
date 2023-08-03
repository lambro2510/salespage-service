package com.salespage.salespageservice.app.responses.storeResponse;

import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import lombok.Data;

@Data
public class StoreInfoResponse {
  private String storeId;
  private String storeName;

  private String imageUrl;

  private Rate rate;

  private String address;

  private String description;

  public void assignFromSellerStore(SellerStore sellerStore) {
    storeId = sellerStore.getId().toHexString();
    imageUrl = sellerStore.getImageUrl();
    storeName = sellerStore.getStoreName();
    rate = sellerStore.getRate();
    address = sellerStore.getAddress();
    description = sellerStore.getDescription();
  }

}
