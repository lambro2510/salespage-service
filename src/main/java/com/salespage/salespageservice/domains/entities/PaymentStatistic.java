package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document("payment_statistic")
@Data
public class PaymentStatistic {
  @Id
  private ObjectId id;

  @Field("daily")
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate daily;

  @Field("product_id")
  private String productId;

  @Field("total_user")
  private Long totalUser = 0L;

  @Field("total_buy")
  private Long totalBuy = 0L;

  @Field("total_purchase")
  private Long totalPurchase = 0L;

  @Field("total_shipper_cod")
  private Long totalShipperCod = 0L;
}
