package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Otp;
import com.salespage.salespageservice.domains.entities.types.OtpStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends MongoRepository<Otp, ObjectId> {
  List<Otp> findByStatus(OtpStatus otpStatus);
}
