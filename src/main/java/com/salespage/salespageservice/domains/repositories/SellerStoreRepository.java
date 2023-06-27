package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.SellerStore;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerStoreRepository extends MongoRepository<SellerStore, ObjectId> {
    Page<SellerStore> findByOwnerStoreName(String username, Pageable pageable);

    List<ObjectId> findIdByOwnerStoreName(String username);

    List<SellerStore> findIdsByStoreName(String storeName);

    List<SellerStore> findIdsByOwnerStoreName(String username);

    List<SellerStore> findByIdIn(List<ObjectId> ids);
}
