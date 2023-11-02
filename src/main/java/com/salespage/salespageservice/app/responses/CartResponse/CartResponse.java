package com.salespage.salespageservice.app.responses.CartResponse;

import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class CartResponse {

  String cartId;

  Boolean canPayment = true;

  String productDetailId;

  String productDetailName;

  String productDetailImageUrl;

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

  List<String> comboIds = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CartResponse)) return false;
    CartResponse that = (CartResponse) o;
    return Objects.equals(getProductId(), that.getProductId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getProductId());
  }
}
