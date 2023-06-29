package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.CheckInDailyStatistic;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInDailyStatisticRepository extends MongoRepository<CheckInDailyStatistic, ObjectId> {
    CheckInDailyStatistic findByUsernameAndMonth(String username, String month);
}
