package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.types.ActiveState;
import com.salespage.salespageservice.domains.entities.types.DiscountType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("product_combo")
@Data
public class ProductCombo extends BaseEntity{

  @Id
  private ObjectId id;

  @Field("combo_name")
  private String comboName;

  @Field("discount_type")
  private DiscountType type;

  @Field("state")
  private ActiveState state;

  @Field("value")
  private Double value;

  @Field("quantity_to_use")
  private Long quantityToUse;

  @Field("max_discount")
  private Double maxDiscount;

  @Field("created_by")
  private String createdBy;
}
