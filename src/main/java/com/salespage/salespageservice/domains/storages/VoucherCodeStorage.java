package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class VoucherCodeStorage extends BaseStorage{
  public void saveAll(List<VoucherCode> voucherCodes) {
    voucherCodeRepository.saveAll(voucherCodes);
  }

  public VoucherCode findCodeCanUse(String username, String code) {
    return voucherCodeRepository.findByOwnerIdAndCodeAndVoucherCodeStatus(username, code, VoucherCodeStatus.OWNER);
  }

  public VoucherCode findFirstVoucherCanUseByVoucherStoreId(String voucherStoreId, Date expireTime) {
    return voucherCodeRepository.findFirstByVoucherStoreIdAndExpireTimeGreaterThanAndVoucherCodeStatus(voucherStoreId, expireTime, VoucherCodeStatus.NEW);
  }

  public void save(VoucherCode voucherCode) {
    voucherCodeRepository.save(voucherCode);
  }

  public Page<VoucherCode> findAll(Query query, Pageable pageable) {
    return voucherCodeRepository.findAll(query,pageable);
  }
}
