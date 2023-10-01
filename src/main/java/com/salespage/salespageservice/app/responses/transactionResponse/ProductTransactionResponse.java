package com.salespage.salespageservice.app.responses.transactionResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class ProductTransactionResponse {

  @Schema(description = "ID giao dịch sản phẩm")
  private String transactionId;

  @Schema(description = "ID sản phẩm")
  private String productId;

  @Schema(description = "Tên sản phẩm")
  private String productName;

  @Schema(description = "Link ảnh sản phẩm")
  private String productImageUrl;

  @Schema(description = "Tổng giá")
  @JsonProperty("total_price")
  private Long totalPrice;

  @Schema(description = "Giá mỗi sản phẩm")
  @JsonProperty("price")
  private Double price;

  @Schema(description = "Tên người bán")
  private String sellerName;

  @Schema(description = "Tên người mua")
  private String buyerName;

  @Schema(description = "Id cửa hàng")
  private String storeId;

  @Schema(description = "Tên cửa hàng")
  private String storeName;

  @Schema(description = "Địa chỉ")
  private String address;

  @Schema(description = "Ghi chú")
  private String note;

  @Schema(description = "Số lượng")
  private Long quantity;

  @Schema(description = "Sử dụng voucher")
  private Boolean isUseVoucher;

  @Schema(description = "Trạng thái giao dịch sản phẩm")
  private ProductTransactionState productTransactionState;

  @Schema(description = "Thông tin voucher")
  private VoucherInfo voucherInfo;

  @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
  @Schema(description = "Ngày tạo")
  @JsonProperty("created_at")
  private Date createdAt;

  public void partnerFromProductTransaction(ProductTransaction productTransaction) {
    transactionId = productTransaction.getId().toHexString();
    productId = productTransaction.getProductId();
    productName = productTransaction.getProduct().getProductName();
    productImageUrl = productTransaction.getProduct().getDefaultImageUrl();
    storeId = productTransaction.getStoreId();
    storeName = productTransaction.getStore().getStoreName();
    totalPrice = productTransaction.getTotalPrice().longValue();
    sellerName = productTransaction.getSellerUsername();
    buyerName = productTransaction.getBuyerUsername();
    quantity = productTransaction.getQuantity();
    isUseVoucher = productTransaction.getIsUseVoucher();
    productTransactionState = productTransaction.getState();
    voucherInfo = productTransaction.getVoucherInfo();
    note = productTransaction.getNote();
    address = productTransaction.getAddressReceive();
    createdAt = new Date(productTransaction.getCreatedAt());
    price = productTransaction.getProduct().getPrice();
  }
}
