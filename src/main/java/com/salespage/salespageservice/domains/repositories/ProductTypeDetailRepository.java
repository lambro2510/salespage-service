package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.ProductType;
import com.salespage.salespageservice.domains.entities.ProductTypeDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeDetailRepository extends MongoRepository<ProductTypeDetail, ObjectId> {
  ProductTypeDetail findProductTypeDetailByTypeNameAndTypeDetailName(String typeName, String typeDetailName);

  ProductTypeDetail findProductTypeDetailById(ObjectId objectId);

  List<ProductTypeDetail> findByProductId(String productId);

  List<ProductTypeDetail> findTop10ByTypeDetailNameInOrderByCreatedAtDesc(List<String> listType);

}
