package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.TpBankTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TpBankTransactionRepository extends MongoRepository<TpBankTransaction, ObjectId> {
  TpBankTransaction findByTransId(String id);
}
