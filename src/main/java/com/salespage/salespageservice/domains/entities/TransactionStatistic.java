package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
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
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  @Field("date")
  private String date;

  @Field("username")
  private String username;

  @Field("product_detail_id")
  private String productDetailId;

  @Field("product_id")
  private String productId;

  @Field("statistic_type")
  private StatisticType statisticType;

  @Field("total_product")
  private Integer totalProduct;

  @Field("total_price")
  private Integer totalPrice;

  @Field("total_user")
  private Integer totalUser;

}
