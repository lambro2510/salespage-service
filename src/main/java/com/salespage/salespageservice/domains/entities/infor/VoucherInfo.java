package com.salespage.salespageservice.domains.entities.infor;

import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherInfo {
    private String voucherCode;

    private VoucherStoreType voucherStoreType;

    private Long value;

    private BigDecimal priceBefore;
    private BigDecimal priceAfter;
}
