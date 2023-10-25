package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.app.dtos.productDtos.ProductTypeDto;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductTypeResponse;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Document("product_type")
@Data
public class ProductType extends BaseEntity {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

  @Indexed(name = "product_type_idx")
  @Field(name = "product_type")
  private String productType;

  @Field(name = "product_type_name")
  private String productTypeName;

  @Field(name = "status")
  private ProductTypeStatus status;

  @Field(name = "description")
  private String description;

  @Field(name = "created_by")
  private String createdBy;

  @Field(name = "updated_by")
  private String updatedBy;

  public void partnerFromDto(ProductTypeDto dto) {
    productType = dto.getProductType();
    productTypeName = dto.getTypeName();
    status = dto.getStatus();
    description = dto.getDescription();
  }

  public ProductTypeResponse partnerToProductTypeResponse() {
    ProductTypeResponse response = new ProductTypeResponse();
    response.setValue(id.toHexString());
    response.setLabel(productType + '-' + productTypeName);
    return response;
  }
}
