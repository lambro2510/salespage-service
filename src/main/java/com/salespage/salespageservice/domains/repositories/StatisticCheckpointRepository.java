package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.StatisticCheckpoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticCheckpointRepository extends MongoRepository<StatisticCheckpoint, String> {
  StatisticCheckpoint findStatisticCheckpointById(String id);
}
