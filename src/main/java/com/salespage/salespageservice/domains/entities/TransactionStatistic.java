package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.types.StatisticType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("transaction_statistic")
@Data
public class TransactionStatistic {
  @Id
  private ObjectId id;

  @Field("date")
  private String date;

  @Field("username")
  private String username;

  @Field("product_id")
  private String productId;

  @Field("statistic_type")
  private StatisticType statisticType;

  @Field("total_product")
  private Long totalProduct;

  @Field("total_price")
  private Long totalPrice;

  @Field("total_user")
  private Long totalUser;

}
