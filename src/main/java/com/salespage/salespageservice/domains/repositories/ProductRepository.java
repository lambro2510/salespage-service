package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {

  Product findProductById(ObjectId id);

  List<ProductTransaction> findAllProductById(ObjectId objectId);
}
