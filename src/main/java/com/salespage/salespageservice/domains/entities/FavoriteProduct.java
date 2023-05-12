package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("favorite_product")
@Data
public class FavoriteProduct extends BaseEntity {

  @Id
  private ObjectId id;

  @Field("user_id")
  private String userId;

  @Field("product_id")
  private String productId;

  @Field("favorite_type")
  private FavoriteType favoriteType;

  @Field("is_like")
  private Boolean isLike = false;

  @Field("rate_star")
  private Float rateStar = 0F;
}
