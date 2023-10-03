package com.salespage.salespageservice.app.responses.Cart;

import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import lombok.Data;

@Data
public class CartResponse {

  Boolean canPayment = true;

  String productId;

  String imageUrl;

  String productName;

  Long quantity = 0L;

  String productNote;

  String voucherNote;

  VoucherInfo voucherInfo;

  Double totalPrice = 0D;
}
