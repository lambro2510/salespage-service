package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productDtos.CreateProductCategoryTypeDto;
import com.salespage.salespageservice.app.dtos.productDtos.UpdateProductCategoryTypeDto;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductCategoryResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.entities.ProductType;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService extends BaseService {
  public void createProductCategory(String username, CreateProductCategoryTypeDto dto) {
    ProductType type = productTypeStorage.findByProductType(dto.getProductType());
    if(Objects.isNull(type)) throw new ResourceNotFoundException("Không tìm thấy loại sản phẩm này");
    ProductCategory productCategory = ProductCategory.builder()
        .categoryName(dto.getCategoryName())
        .categoryType(dto.getCategoryType())
        .productType(dto.getProductType())
        .description(dto.getDescription())
        .createdBy(username)
        .updatedBy(username)
        .timeType(dto.getTimeType())
        .timeValue(dto.getTimeValue())
        .build();
  productCategoryStorage.save(productCategory);

  }

  @Transactional
  public void updateProductCategory(String username, UpdateProductCategoryTypeDto dto) {
    ProductType type = productTypeStorage.findByProductType(dto.getProductType());
    if(Objects.isNull(type)) throw new ResourceNotFoundException("Không tìm thấy loại sản phẩm này");

    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, dto.getId());
    if(Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm");

    productCategory.setCategoryName(dto.getCategoryName());
    productCategory.setProductType(dto.getProductType());
    productCategory.setCategoryType(dto.getCategoryType());
    productCategory.setDescription(dto.getDescription());
    productCategory.setTimeType(dto.getTimeType());
    productCategory.setTimeValue(dto.getTimeValue());
    productCategory.setUpdatedBy(username);
    productCategory.setUpdatedAt(System.currentTimeMillis());
    productCategoryStorage.save(productCategory);
  }

  public void deleteProductCategory(String username, String id) {
    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, id);
    if(Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không timd thấy danh mục sản phẩm");
    productCategoryStorage.delete(productCategory);

  }

  public List<ProductCategoryResponse> getProductCategory(String username) {
    return productCategoryStorage.findByCreatedBy(username).stream().map(ProductCategory::partnerToResponse).collect(Collectors.toList());
  }

  public ProductCategoryResponse getDetailProductCategory(String username, String id) {
    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, id);
    if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm");
    ProductCategoryResponse response = new ProductCategoryResponse();
    response.partnerFromCategory(productCategory);
    return response;
  }
}
