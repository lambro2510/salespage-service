package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
  User findByUsername(String username);

    User findUserById(ObjectId userId);
}
