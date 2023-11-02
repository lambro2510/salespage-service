package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductCategory;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.bson.types.ObjectId;
import com.salespage.salespageservice.domains.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SellerStoreStorage extends BaseStorage {

  public Page<SellerStore> findByOwnerStoreName(String username, Pageable pageable) {
    return sellerStoreRepository.findByOwnerStoreName(username, pageable);
  }

  public List<ObjectId> findByOwnerStoreName(String username) {
    return sellerStoreRepository.findIdByOwnerStoreName(username);
  }

  public void save(SellerStore sellerStore) {
    sellerStoreRepository.save(sellerStore);
    remoteCacheManager.del(CacheKey.genSellerStoreId(sellerStore.getId().toHexString()));
    remoteCacheManager.del(CacheKey.genSellerStoreName(sellerStore.getStoreName()));
  }

  public SellerStore findById(String storeId) {
    String key = CacheKey.genSellerStoreId(storeId);
    SellerStore sellerStore = remoteCacheManager.get(key, SellerStore.class);
    if(sellerStore == null){
      sellerStore =  sellerStoreRepository.findById(new ObjectId(storeId)).orElse(null);
      remoteCacheManager.set(key, sellerStore);
    }
    return sellerStore;
  }

  public List<SellerStore> findIdsByStoreName(String storeName) {
    String key = CacheKey.genSellerStoreName(storeName);
    List<SellerStore> sellerStores = remoteCacheManager.getList(key, SellerStore.class);
    if(sellerStores == null){
      sellerStores =  sellerStoreRepository.findIdsByStoreName(storeName);
      remoteCacheManager.set(key, sellerStores);
    }
    return sellerStores;
  }

  public List<SellerStore> findIdsByOwnerStoreName(String username) {
    return sellerStoreRepository.findIdsByOwnerStoreName(username);
  }

  public Page<SellerStore> findAll(Pageable newPageable) {
    return sellerStoreRepository.findAll(newPageable);
  }

  public List<SellerStore> findByIdIn(List<String> ids) {
    return sellerStoreRepository.findByIdIn((Helper.convertListStringToListObjectId(ids)));
  }

  public List<SellerStore> findSellerStoreByIdIn(List<String> ids) {
    String key = CacheKey.genSellerStoreByIdIn(ids);
    List<SellerStore> sellerStores = remoteCacheManager.getList(key, SellerStore.class);
    if(sellerStores == null){
      sellerStores = sellerStoreRepository.findByIdIn((Helper.convertListStringToListObjectId(ids)));
      remoteCacheManager.set(key, sellerStores);
    }
    return sellerStores;
  }

  public boolean isExistByStoreId(String refId) {
    return sellerStoreRepository.existsById(new ObjectId(refId));
  }

  public void delete(SellerStore sellerStore) {
    sellerStoreRepository.delete(sellerStore);
    remoteCacheManager.del(CacheKey.genSellerStoreId(sellerStore.getId().toHexString()));
    remoteCacheManager.del(CacheKey.genSellerStoreName(sellerStore.getStoreName()));
  }
}
