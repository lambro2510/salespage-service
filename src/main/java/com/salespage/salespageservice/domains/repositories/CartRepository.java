package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<Cart, ObjectId> {
  List<Cart> findByUsername(String username);

  Long countByUsername(String username);

  Page<Cart> findByUsername(String username, Pageable pageable);
}
