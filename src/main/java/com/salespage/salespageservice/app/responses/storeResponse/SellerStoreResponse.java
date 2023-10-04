package com.salespage.salespageservice.app.responses.storeResponse;

import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.status.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SellerStoreResponse {

  @Schema(description = "ID cửa hàng")
  private String id;

  private String storeName;

  private String address;

  private String description;

  private String location;

  private StoreStatus status;

  @Schema(description = "URL ảnh cửa hàng")
  private String imageUrl;


}
