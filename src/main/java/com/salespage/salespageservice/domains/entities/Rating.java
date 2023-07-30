package com.salespage.salespageservice.domains.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating extends BaseEntity{

  @Id
  private ObjectId id;

  @Field("username")
  private String username;

  @Field("product_id")
  private String productId;

  @Field("point")
  private Long point;
}
