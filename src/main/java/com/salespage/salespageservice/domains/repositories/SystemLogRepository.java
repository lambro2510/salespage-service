package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.SystemLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogRepository extends MongoRepository<SystemLog, ObjectId> {
}
