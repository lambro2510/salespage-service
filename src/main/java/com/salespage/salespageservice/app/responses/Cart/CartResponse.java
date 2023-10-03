package com.salespage.salespageservice.app.responses.Cart;

import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import lombok.Data;

@Data
public class CartResponse {

  String cartId;

  Boolean canPayment = true;

  String productId;

  String storeId;

  String storeName;

  String categoryId;

  String categoryName;

  String imageUrl;

  String productName;

  Long quantity = 0L;

  String productNote;

  String voucherNote;

  VoucherInfo voucherInfo;

  Double totalPrice = 0D;
}
