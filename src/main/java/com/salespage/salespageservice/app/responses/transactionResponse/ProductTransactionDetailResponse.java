package com.salespage.salespageservice.app.responses.transactionResponse;

import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class ProductTransactionDetailResponse {
  private String id;

  private String productDetailId;

  private ProductDetail productDetail;

  private String productId;

  private Product product;

  private String transactionId;

  private String storeId;

  private SellerStore store;

  private ProductTransactionState state;

  private Long quantity = 0L;

  private Double totalPrice = 0D;

  private String note;

  private VoucherInfo voucherInfo;

  private Double shipCod;

  private String username;

  private Long createdAt;

  private String address;
}
