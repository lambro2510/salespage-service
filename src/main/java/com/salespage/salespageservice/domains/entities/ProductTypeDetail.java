package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Data
@Document("product_type_detail")
public class ProductTypeDetail extends BaseEntity {
  @Id
  private ObjectId id;

  @Field("type_name")
  private String typeName;

  @Field("note")
  private String note;

  @Field("status")
  private ProductTypeStatus status;

  @Field(name = "created_by")
  private String createdBy;

  @Field(name = "udpated_by")
  private Long updatedBy;

  @Field(name = "accepted_by")
  private String acceptedBy;

  @Field(name = "accepted_at")
  private Long acceptedAt;
}
