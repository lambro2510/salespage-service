package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.status.ProductTypeDetailStatus;
import lombok.Data;

@Data
public class UpdateTypeDetailStatusDto {
    String id;
    ProductTypeDetailStatus status;
}
