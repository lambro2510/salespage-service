package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends MongoRepository<ProductType, String> {
  ProductType findByProductType(String productType);
}
