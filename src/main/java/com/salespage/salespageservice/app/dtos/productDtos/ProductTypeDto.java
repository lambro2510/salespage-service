package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import lombok.Data;

@Data
public class ProductTypeDto {
    String productType;

    String typeName;

    String description;

    ProductTypeStatus status;
}
