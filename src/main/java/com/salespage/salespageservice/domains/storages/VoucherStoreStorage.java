package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.VoucherStore;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoucherStoreStorage extends BaseStorage {
  public void save(VoucherStore voucherStore) {
    voucherStoreRepository.save(voucherStore);
    remoteCacheManager.del(CacheKey.genVoucherStoreById(voucherStore.getId().toHexString()));
  }

  public VoucherStore findVoucherStoreById(String voucherStoreId) {
    String key = CacheKey.genVoucherStoreById(voucherStoreId);
    VoucherStore voucherStore = remoteCacheManager.get(key, VoucherStore.class);
    if (voucherStore == null) {
      voucherStore = voucherStoreRepository.findVoucherStoreById(voucherStoreId);
      remoteCacheManager.set(key, voucherStore);
    }
    return voucherStore;
  }

  public void deleteVoucherStoreById(String voucherStoreId) {
    voucherStoreRepository.deleteVoucherStoreById(voucherStoreId);
  }

  public List<VoucherStore> findVoucherStoreByCreatedBy(String username) {
    return voucherStoreRepository.findVoucherStoreByCreatedBy(username);
  }

  public List<VoucherStore> findByVoucherStoreTypeAndRefId(VoucherStoreType voucherStoreType, String refId) {
    return voucherStoreRepository.findByVoucherStoreTypeAndRefId(voucherStoreType, refId);
  }

  public Page<VoucherStore> findVoucherStoreByCreatedBy(String username, Pageable pageable) {
    return voucherStoreRepository.findVoucherStoreByCreatedBy(username, pageable);

  }

  public List<VoucherStore> findByProductIdIn(List<String> productIds) {
    return voucherStoreRepository.findByVoucherStoreTypeAndRefIdIn(VoucherStoreType.PRODUCT, Helper.convertListStringToListObjectId(productIds));
  }
}
