package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.types.RatingType;
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

  @Field("ref_id")
  private String refId;

  @Field("rating_type")
  private RatingType ratingType;

  @Field("point")
  private Float point;

}
