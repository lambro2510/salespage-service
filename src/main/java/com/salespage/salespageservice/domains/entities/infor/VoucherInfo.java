package com.salespage.salespageservice.domains.entities.infor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.VoucherStore;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherInfo {
  @JsonProperty("isUse")
  private Boolean isUse = false;
  private String code;
  private VoucherStoreType voucherStoreType;
  private DiscountType discountType;
  private Double totalDiscount = 0D;
  private Double priceBefore = 0D;
  private Double priceAfter = 0D;
  private Double value = 0D;
  private String voucherName;

  public VoucherInfo(VoucherCode voucherCode, VoucherStore voucherStore, Double sellPrice) {
    isUse = true;
    code = voucherCode.getCode();
    voucherStoreType = voucherStore.getVoucherStoreType();
    discountType = voucherStore.getDiscountType();
    value = voucherStore.getValue();
    voucherName = voucherStore.getVoucherStoreName();
    priceBefore = sellPrice;
    if (voucherStore.getDiscountType().equals(DiscountType.PERCENT)) {
      totalDiscount = priceBefore * voucherStore.getValue();
    } else if (voucherStore.getDiscountType().equals(DiscountType.TOTAL)) {
      totalDiscount = voucherStore.getValue();
    }
    priceAfter = priceBefore - totalDiscount;

  }
}
