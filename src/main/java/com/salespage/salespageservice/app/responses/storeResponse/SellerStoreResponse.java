package com.salespage.salespageservice.app.responses.storeResponse;

import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.status.StoreStatus;
import lombok.Data;

@Data
public class SellerStoreResponse {
  private String storeId;

  private String storeName;

  private String address;

  private String description;

  private StoreStatus status;

  private String imageUrl;

  public void assignFromSellerStore(SellerStore sellerStore) {
    storeId = sellerStore.getId().toHexString();
    imageUrl = sellerStore.getImageUrl();
    storeName = sellerStore.getStoreName();
    address = sellerStore.getAddress();
    description = sellerStore.getDescription();
    status = sellerStore.getStatus();
  }
}
