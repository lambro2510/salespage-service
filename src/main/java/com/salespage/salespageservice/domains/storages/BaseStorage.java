package com.salespage.salespageservice.domains.storages;


import com.salespage.salespageservice.domains.repositories.*;
import com.salespage.salespageservice.domains.utils.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseStorage {

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ProductTransactionRepository productTransactionRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected VoucherStoreRepository voucherStoreRepository;

    @Autowired
    protected VoucherCodeRepository voucherCodeRepository;

    @Autowired
    protected  SellerStoreRepository sellerStoreRepository;

    @Autowired
    protected RemoteCacheManager remoteCacheManager;
}
