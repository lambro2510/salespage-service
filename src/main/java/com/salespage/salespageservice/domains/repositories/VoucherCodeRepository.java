package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface VoucherCodeRepository extends MongoRepository<VoucherCode, ObjectId> {
  VoucherCode findByOwnerIdAndCode(String username, String code);

  VoucherCode findFirstByVoucherStoreIdAndExpireTimeGreaterThan(String voucherStoreId, Date expireTime);
}
