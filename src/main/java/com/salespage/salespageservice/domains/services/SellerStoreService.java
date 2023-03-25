package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.SellerStoreDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.storeResponse.StoreDataResponse;
import com.salespage.salespageservice.domains.entities.SellerStore;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerStoreService extends BaseService{

  public ResponseEntity<PageResponse<StoreDataResponse>> getAllStore(String username, Pageable pageable) {
    Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    Page<SellerStore> sellerStores = sellerStoreStorage.findByOwnerStoreName(username, newPageable);
    List<SellerStore> sellerStoreList = sellerStores.getContent();

    List<StoreDataResponse> storeDataResponses = new ArrayList<>();
    for(SellerStore sellerStore : sellerStoreList){
      StoreDataResponse storeDataResponse = new StoreDataResponse();
      storeDataResponse.assignFromSellerStore(sellerStore);
      storeDataResponses.add(storeDataResponse);
    }

    Page<StoreDataResponse> pageStoreDataResponse = new PageImpl<>(storeDataResponses, newPageable, sellerStores.getTotalElements());
    return ResponseEntity.ok(PageResponse.createFrom(pageStoreDataResponse));
  }

  public ResponseEntity<?> createStore(String username, SellerStoreDto sellerStoreDto) {
    SellerStore sellerStore = new SellerStore();
    sellerStore.assignFromSellerStoreDto(sellerStoreDto);
    sellerStore.setOwnerStoreName(username);
    sellerStoreStorage.save(sellerStore);
    return ResponseEntity.ok(true);
  }

  public List<ObjectId> findIdByStoreName(String storeName) {
    return sellerStoreStorage.findIdByStoreName(storeName);
  }

  public List<ObjectId> findIdByOwnerStoreId(String username) {
    return sellerStoreStorage.findByOwnerStoreName(username);
  }
}
