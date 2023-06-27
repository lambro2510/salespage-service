package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.VoucherStore;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherStoreRepository extends MongoRepository<VoucherStore, ObjectId> {
    VoucherStore findVoucherStoreById(String voucherStoreId);

    void deleteVoucherStoreById(String voucherStoreId);

    List<VoucherStore> findVoucherStoreByCreatedBy(String username);
}
