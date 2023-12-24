package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.VoucherCodeLimit;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoucherCodeLimitStorage extends BaseStorage {
  public VoucherCodeLimit findByUsernameAndVoucherStoreId(String username, String voucherStoreId) {
    return voucherCodeLimitRepository.findByUsernameAndVoucherStoreId(username, voucherStoreId);
  }

  public void save(VoucherCodeLimit voucherCodeLimit) {
    voucherCodeLimitRepository.save(voucherCodeLimit);
  }

  public List<VoucherCodeLimit> findByUsernameAndVoucherStoreIdIn(String username, List<String> voucherStoreIds) {
    return voucherCodeLimitRepository.findByUsernameAndVoucherStoreIdIn(username, voucherStoreIds);
  }
}
