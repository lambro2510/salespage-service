package com.salespage.salespageservice.domains.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("product_combo_detail")
@Data
public class ProductComboDetail extends BaseEntity{

  @Id
  private ObjectId id;

  @Field("combo_id")
  private String comboId;

  @Field("product_id")
  private String productId;
}
