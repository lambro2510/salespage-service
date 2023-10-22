package com.salespage.salespageservice.app.responses.CartResponse;

import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import lombok.Data;

@Data
public class CartResponse {

  String cartId;

  Boolean canPayment = true;

  String productDetailId;

  String productId;

  String storeId;

  String storeName;

  String categoryId;

  String categoryName;

  Double price;

  Double sellPrice;

  Double discountPercent;

  String imageUrl;

  String productName;

  Long quantity = 0L;

  Integer limit = 0;

  String productNote;

  String voucherNote;

  VoucherInfo voucherInfo;

  Double totalPrice = 0D;
}
