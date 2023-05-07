package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductType;
import com.salespage.salespageservice.domains.entities.status.ProductTypeStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends MongoRepository<ProductType, String> {
  ProductType findByProductType(String productType);

  List<ProductType> findByStatus(ProductTypeStatus status);
}
