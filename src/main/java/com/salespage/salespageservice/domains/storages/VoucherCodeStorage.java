package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class VoucherCodeStorage extends BaseStorage {
  public void saveAll(List<VoucherCode> voucherCodes) {
    voucherCodeRepository.saveAll(voucherCodes);
  }

  public VoucherCode findCodeCanUse(String username, String code) {
    return voucherCodeRepository.findByUsernameAndCodeAndVoucherCodeStatusAndExpireTimeGreaterThan(username, code, VoucherCodeStatus.OWNER, DateUtils.now().toLocalDate());
  }

  public VoucherCode findFirstCodeCanUse(String username, String storeId) {
    return voucherCodeRepository.findFirstByUsernameAndVoucherStoreIdAndVoucherCodeStatusAndExpireTimeGreaterThan(username, storeId, VoucherCodeStatus.OWNER, DateUtils.now().toLocalDate());
  }

  public VoucherCode findFirstVoucherCanUseByVoucherStoreId(String voucherStoreId, LocalDate expireTime) {
    return voucherCodeRepository.findFirstByVoucherStoreIdAndExpireTimeGreaterThanAndVoucherCodeStatus(voucherStoreId, expireTime, VoucherCodeStatus.NEW);
  }

  public void save(VoucherCode voucherCode) {
    voucherCodeRepository.save(voucherCode);
    remoteCacheManager.del(CacheKey.genVoucherCodeById(voucherCode.getId().toHexString()));
  }

  public Page<VoucherCode> findAll(Query query, Pageable pageable) {
    return voucherCodeRepository.findAll(query, pageable);
  }

  public VoucherCode findById(String voucherCodeId) {
    String key = CacheKey.genVoucherCodeById(voucherCodeId);
    VoucherCode voucherCode = remoteCacheManager.get(key, VoucherCode.class);
    if(voucherCode == null){
      voucherCode = voucherCodeRepository.findById(new ObjectId(voucherCodeId)).get();
      remoteCacheManager.set(key,voucherCode);
    }
    return voucherCode;
  }
}
