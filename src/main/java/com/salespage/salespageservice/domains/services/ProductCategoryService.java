package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.ProductCategories.ProductCategoryDto;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductCategoryResponse;
import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.entities.ProductType;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductCategoryService extends BaseService {
  public void createProductCategory(String username, ProductCategoryDto dto) {
    ProductType type = productTypeStorage.findProductTypeById(dto.getProductTypeId());
    if (Objects.isNull(type)) throw new ResourceNotFoundException("Không tìm thấy loại sản phẩm này");
    ProductCategory productCategory = modelMapper.toProductCategory(dto);
    productCategory.setCreatedBy(username);
    productCategory.setUpdatedBy(username);
    productCategoryStorage.save(productCategory);

  }

  public void updateProductCategory(String username, String categoryId, ProductCategoryDto dto) {
    ProductType type = productTypeStorage.findProductTypeById(dto.getProductTypeId());
    if (Objects.isNull(type)) throw new ResourceNotFoundException("Không tìm thấy loại sản phẩm này");

    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, categoryId);
    if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm");

    modelMapper.mapToProductCategory(dto, productCategory);
    productCategory.setUpdatedBy(username);
    productCategoryStorage.save(productCategory);
  }

  public void deleteProductCategory(String username, String id) {
    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, id);
    if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không timd thấy danh mục sản phẩm");
    productCategoryStorage.delete(productCategory);

  }

  public List<ProductCategoryResponse> getProductCategory(String username) {
    List<ProductCategoryResponse> responses = modelMapper.toListProductCategoryResponse(productCategoryStorage.findByCreatedBy(username));
    for (ProductCategoryResponse response : responses) {
      ProductType type = productTypeStorage.findProductTypeById(response.getProductTypeId());
      if (type != null) {
        response.setProductType(type.getProductType());
        response.setProductTypeName(type.getProductTypeName());
      }
    }
    return responses;
  }

  public ProductCategoryResponse getDetailProductCategory(String username, String id) {
    ProductCategory productCategory = productCategoryStorage.findByCreatedByAndId(username, id);
    if (Objects.isNull(productCategory)) throw new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm");
    return modelMapper.toProductCategoryResponse(productCategory);
  }
}
