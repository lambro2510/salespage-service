package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class VoucherCodeStorage extends BaseStorage {
  public void saveAll(List<VoucherCode> voucherCodes) {
    voucherCodeRepository.saveAll(voucherCodes);
  }

  public VoucherCode findCodeCanUse(String username, String code) {
    return voucherCodeRepository.findByOwnerIdAndCodeAndVoucherCodeStatusAndExpireTimeGreaterThan(username, code, VoucherCodeStatus.OWNER, new Date());
  }

  public VoucherCode findFirstCodeCanUse(String username, String storeId) {
    return voucherCodeRepository.findFirstByOwnerIdAndVoucherStoreIdAndVoucherCodeStatusAndExpireTimeGreaterThan(username, storeId, VoucherCodeStatus.OWNER, new Date());
  }

  public VoucherCode findFirstVoucherCanUseByVoucherStoreId(String voucherStoreId, Date expireTime) {
    return voucherCodeRepository.findFirstByVoucherStoreIdAndExpireTimeGreaterThanAndVoucherCodeStatus(voucherStoreId, expireTime, VoucherCodeStatus.NEW);
  }

  public void save(VoucherCode voucherCode) {
    voucherCodeRepository.save(voucherCode);
  }

  public Page<VoucherCode> findAll(Query query, Pageable pageable) {
    return voucherCodeRepository.findAll(query, pageable);
  }

  public VoucherCode findById(String voucherCodeId) {
    return voucherCodeRepository.findById(new ObjectId(voucherCodeId)).get();
  }
}
