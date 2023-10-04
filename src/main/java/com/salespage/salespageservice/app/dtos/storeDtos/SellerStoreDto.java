package com.salespage.salespageservice.app.dtos.storeDtos;

import com.salespage.salespageservice.domains.entities.status.StoreStatus;
import lombok.Data;

@Data
public class SellerStoreDto {

  private String storeName;

  private String address;

  private String description;

  private String location;

  private StoreStatus status;


}
