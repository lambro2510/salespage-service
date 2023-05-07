package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("product_type")
@Data
public class ProductType extends BaseEntity {

  @Id
  @Field(name = "product_type")
  private String productType;

  @Field(name = "product_type_name")
  private String productTypeName;

  @Field(name = "status")
  private ProductTypeStatus status;

  @Field(name = "created_by")
  private String createdBy;

  @Field(name = "udpated_by")
  private Long updatedBy;
}
