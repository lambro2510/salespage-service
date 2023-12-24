package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import com.salespage.salespageservice.domains.entities.status.VoucherCodeStatus;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.DateUtils;
import com.salespage.salespageservice.domains.utils.Helper;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class VoucherCodeStorage extends BaseStorage {
  public void saveAll(List<VoucherCode> voucherCodes) {
    voucherCodeRepository.saveAll(voucherCodes);
  }

  public VoucherCode findCodeCanUse(String username, String code) {
    return voucherCodeRepository.findByUsernameAndCodeAndVoucherCodeStatusAndExpireTimeGreaterThan(username, code, VoucherCodeStatus.OWNER, DateUtils.nowInMillis());
  }

  public VoucherCode findFirstCodeCanUse(String username, String storeId) {
    return voucherCodeRepository.findFirstByUsernameAndVoucherStoreIdAndVoucherCodeStatusAndExpireTimeGreaterThan(username, storeId, VoucherCodeStatus.OWNER, DateUtils.nowInMillis());
  }

  public VoucherCode findFirstVoucherCanUseByVoucherStoreId(String voucherStoreId, Long expireTime) {
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
    if (voucherCode == null) {
      voucherCode = voucherCodeRepository.findById(new ObjectId(voucherCodeId)).orElse(null);
      remoteCacheManager.set(key, voucherCode);
    }
    return voucherCode;
  }

  public List<VoucherCode> findByVoucherStoreIdInAndUserName(List<String> ids, String username) {
    String key = CacheKey.genVoucherCodeByIdInAndUsername(ids, username);
    List<VoucherCode> voucherCodes = remoteCacheManager.getList(key, VoucherCode.class);
    if (voucherCodes == null) {
      voucherCodes = voucherCodeRepository.findByVoucherStoreIdInAndUsername(ids, username);
      remoteCacheManager.set(key, voucherCodes);
    }
    return voucherCodes;
  }
}
