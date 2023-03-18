package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherCodeRepository extends MongoRepository<VoucherCode, ObjectId> {
}
