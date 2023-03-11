package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.types.ProductType;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("voucher_store")
public class VoucherStore {
  @Id
  private ObjectId id;

  @Field("voucher_store_name")
  private String voucherStoreName;

  @Field("quantity")
  private Long quantity;

  @Field("description")
  private String description;

  public static class VoucherDetail{
    @Field("product_type")
    private ProductType productType;

    @Field("product_id")
    private String productId;

    @Field("is_limit_price")
    private Boolean isLimitPrice;

    @Field("is_enable")
    private Boolean isEnable;

    @Field("min_price")
    private Long minPrice;

    @Field("max_price")
    private Long maxPrice;

    @Field("limit_voucher_per_user")
    private Long limitVoucherPerUser;
  }

}
