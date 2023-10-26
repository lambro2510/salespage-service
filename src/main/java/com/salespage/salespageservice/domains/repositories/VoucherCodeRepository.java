package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface VoucherCodeRepository extends MongoRepository<VoucherCode, ObjectId> {

  VoucherCode findFirstByVoucherStoreIdAndExpireTimeGreaterThanAndVoucherCodeStatus(String voucherStoreId, LocalDate expireTime, VoucherCodeStatus voucherCodeStatus);

  Page<VoucherCode> findAll(Query query, Pageable pageable);

  VoucherCode findByUsernameAndCodeAndVoucherCodeStatusAndExpireTimeGreaterThan(String username, String code, VoucherCodeStatus voucherCodeStatus, LocalDate expireTime);

  VoucherCode findFirstByUsernameAndCodeAndVoucherCodeStatusAndExpireTimeGreaterThan(String username, String code, VoucherCodeStatus voucherCodeStatus, Date now);

  VoucherCode findFirstByUsernameAndVoucherStoreIdAndVoucherCodeStatusAndExpireTimeGreaterThan(String username, String voucherStoreId, VoucherCodeStatus voucherCodeStatus, LocalDate expireTime);

  List<VoucherCode> findByVoucherStoreIdInAndUsername(List<ObjectId> objectIds, String username);
}
