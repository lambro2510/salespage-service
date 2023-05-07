package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductType;
import com.salespage.salespageservice.domains.entities.ProductTypeDetail;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeStorage extends BaseStorage {

  public void save(ProductType productType) {
    productTypeRepository.save(productType);
  }

  public ProductType findByProductType(String productType) {
    return productTypeRepository.findByProductType(productType);
  }

  public void save(ProductTypeDetail productTypeDetail) {
    productTypeDetailRepository.save(productTypeDetail);
  }

  public ProductTypeDetail findProductTypeDetailByTypeNameAndTypeDetailName(String typeName, String typeDetailName) {
    return productTypeDetailRepository.findProductTypeDetailByTypeNameAndTypeDetailName(typeName, typeDetailName);
  }
}
