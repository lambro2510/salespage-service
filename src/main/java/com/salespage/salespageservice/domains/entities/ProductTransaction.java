package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.app.responses.transactionResponse.ProductTransactionResponse;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("product_transaction")
@Data
public class ProductTransaction extends BaseEntity {

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;

  @Field("buyer_username")
  @Indexed(name = "buyer_username_idx")
  private String buyerUsername;

  @Field("product_id")
  @Indexed(name = "product_id_idx")
  private String productId;

  @Field("seller_username")
  private String sellerUsername;

  @Field("store_id")
  private String storeId;

  @Field("store")
  private SellerStore store;

  @Field("product")
  private Product product;

  @Field("state")
  private ProductTransactionState state;

  @Field("quantity")
  private Long quantity = 0L;

  @Field("total_price")
  private Double totalPrice = 0D;

  @Field("ship_cod")
  private Double shipCod;

  @Field("address_receive")
  private String addressReceive;

  @Field("note")
  private String note;

  @Field("is_use_voucher")
  private Boolean isUseVoucher = false;

  @Field("voucher_info")
  private VoucherInfo voucherInfo;

  @Field("shipper_username")
  private String shipperUsername;

  @Field("message")
  private List<Message> messages = new ArrayList<>();

  public ProductTransactionResponse partnerToProductTransactionResponse(){
    ProductTransactionResponse productTransactionResponse = new ProductTransactionResponse();
    productTransactionResponse.partnerFromProductTransaction(this);
    return productTransactionResponse;
  }

  public void createNewTransaction(String username, ProductTransactionDto dto) {
    buyerUsername = username;
    productId = dto.getProductId();
    quantity = dto.getQuantity();
    note = dto.getNote();
    addressReceive = dto.getAddress();
    state = ProductTransactionState.WAITING_STORE;
  }

  public void createAddToCart(String username, ProductTransactionDto dto) {
    buyerUsername = username;
    productId = dto.getProductId();
    quantity = dto.getQuantity();
    note = dto.getNote();
    addressReceive = dto.getAddress();
    state = ProductTransactionState.IN_CART;
  }

  public void updateTransaction(ProductTransactionInfoDto dto) {
    updateQuantity(dto.getQuantity());
    note = dto.getNote();
    addressReceive = dto.getAddress();
  }

  public void updateState(ProductTransactionState state, String note) {
    this.state = state;
    this.note = note;
  }

  public void updateQuantity(long newQuantity){
    quantity = newQuantity;
    double rate = (double) newQuantity / (double) quantity;
    totalPrice = totalPrice/rate;
  }
  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class Message extends BaseEntity {
    private String sender;

    private String receiver;

    private String content;

  }
}
