package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {

  Product findProductById(ObjectId id);

}
