package com.salespage.salespageservice.app.responses.storeResponse;

import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.status.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SellerStoreResponse {

  @Schema(description = "ID cửa hàng")
  private String storeId;

  @Schema(description = "Tên cửa hàng")
  private String storeName;

  @Schema(description = "Địa chỉ cửa hàng")
  private String address;

  @Schema(description = "Mô tả cửa hàng")
  private String description;

  @Schema(description = "Trạng thái cửa hàng")
  private StoreStatus status;

  @Schema(description = "URL ảnh cửa hàng")
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
