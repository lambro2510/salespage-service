package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.info.ProductInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDto{

  private String productName;

  private String description;

  private String categoryId;

  private String sellerUsername;

  private List<String> sellerStoreIds;

  private List<ProductInfo> productInfos = new ArrayList<>();

}
