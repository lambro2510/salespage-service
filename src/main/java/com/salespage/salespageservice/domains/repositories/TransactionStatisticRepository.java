package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.TransactionStatistic;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionStatisticRepository extends MongoRepository<TransactionStatistic, ObjectId> {
  TransactionStatistic findByDate(String date);
}
