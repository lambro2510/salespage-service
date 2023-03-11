package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.LimitVoucher;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitVoucherRepository extends MongoRepository<LimitVoucher, ObjectId> {
}
