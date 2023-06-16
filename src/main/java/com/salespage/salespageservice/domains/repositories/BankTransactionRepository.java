package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.BankTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankTransactionRepository extends MongoRepository<BankTransaction, ObjectId> {
  BankTransaction findByDescriptionLike(String description);
}
