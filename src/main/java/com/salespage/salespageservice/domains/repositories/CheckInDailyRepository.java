package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.CheckInDaily;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CheckInDailyRepository extends MongoRepository<CheckInDaily, ObjectId> {
    CheckInDaily findByUsernameAndDate(String username, String today);

    List<CheckInDaily> findByDateAndCheckInToday(Date date, boolean b);
}
