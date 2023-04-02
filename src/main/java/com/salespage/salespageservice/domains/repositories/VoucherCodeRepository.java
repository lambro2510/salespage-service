package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface VoucherCodeRepository extends MongoRepository<VoucherCode, ObjectId> {

  VoucherCode findFirstByVoucherStoreIdAndExpireTimeGreaterThanAndVoucherCodeStatus(String voucherStoreId, Date expireTime, VoucherCodeStatus voucherCodeStatus);

  Page<VoucherCode> findAll(Query query, Pageable pageable);

  VoucherCode findByOwnerIdAndCodeAndVoucherCodeStatus(String username, String code, VoucherCodeStatus voucherCodeStatus);
}
