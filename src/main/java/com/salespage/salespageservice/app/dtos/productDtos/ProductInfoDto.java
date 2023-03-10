package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.types.ProductType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductInfoDto {
  private String productName;

    private String description;

    private ProductType type;

    private Double price;

    private String sellingAddress;

}
