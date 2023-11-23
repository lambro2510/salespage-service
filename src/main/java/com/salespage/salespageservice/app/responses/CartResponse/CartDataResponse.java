package com.salespage.salespageservice.app.responses.CartResponse;

import com.salespage.salespageservice.domains.entities.infor.ComboInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDataResponse {
  private String cartId;

  private Long productQuantity;

  private String productDetailId;

  private String productName;

  private String productId;

  private String storeId;

  private String storeName;

  private String imageUrl;

  private Double sellPrice;

  private String type;

  private Integer quantity;

  private List<ListProductCartResponse> refCarts = new ArrayList<>();

  private List<ComboInfo> comboInfos = new ArrayList<>();
}
