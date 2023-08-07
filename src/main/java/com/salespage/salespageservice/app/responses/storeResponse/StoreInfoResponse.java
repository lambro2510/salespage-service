package com.salespage.salespageservice.app.responses.storeResponse;

import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StoreInfoResponse {

  @Schema(description = "ID cửa hàng")
  private String storeId;

  @Schema(description = "Tên cửa hàng")
  private String storeName;

  @Schema(description = "URL ảnh cửa hàng")
  private String imageUrl;

  @Schema(description = "Đánh giá cửa hàng")
  private Rate rate;

  @Schema(description = "Địa chỉ cửa hàng")
  private String address;

  @Schema(description = "Mô tả cửa hàng")
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
