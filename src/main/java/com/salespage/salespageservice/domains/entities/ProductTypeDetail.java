package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.app.dtos.productDtos.ProductTypeDetailDto;
import com.salespage.salespageservice.domains.entities.status.ProductTypeDetailStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
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

  @Field("product_id")
  private String productId;

  @Field("type_detail_name")
  private String typeDetailName;

  @Field("note")
  private String note;

  @Field("status")
  @Indexed(name = "status_idx")
  private ProductTypeDetailStatus status;

  @Field(name = "created_by")
  private String createdBy;

  @Field(name = "udpated_by")
  private String updatedBy;

  @Field(name = "accepted_by")
  private String acceptedBy;

  @Field(name = "accepted_at")
  private Long acceptedAt;

  public void partnerFromDto(ProductTypeDetailDto dto) {
    typeName = dto.getTypeName();
    typeDetailName = dto.getTypeDetailName();
    productId = dto.getProductId();
    note = dto.getNote();
    status = ProductTypeDetailStatus.INACTIVE;
  }
}
