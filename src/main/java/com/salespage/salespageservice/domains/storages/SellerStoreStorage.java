package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.SellerStore;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

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
  }

  public SellerStore findById(String storeId) {
    return sellerStoreRepository.findById(new ObjectId(storeId)).get();
  }

  public List<SellerStore> findIdsByStoreName(String storeName) {
    return sellerStoreRepository.findIdsByStoreName(storeName);
  }

  public List<SellerStore> findIdsByOwnerStoreName(String username) {
    return sellerStoreRepository.findIdsByOwnerStoreName(username);
  }

  public Page<SellerStore> findAll(Pageable newPageable) {
    return sellerStoreRepository.findAll(newPageable);
  }
}
