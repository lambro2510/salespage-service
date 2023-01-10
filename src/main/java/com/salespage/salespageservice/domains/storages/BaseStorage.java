package com.salespage.salespageservice.domains.storages;


import com.salespage.salespageservice.domains.repositories.AccountRepository;
import com.salespage.salespageservice.domains.repositories.ProductRepository;
import com.salespage.salespageservice.domains.repositories.ProductTransactionRepository;
import com.salespage.salespageservice.domains.repositories.UserRepository;
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

//    @Autowired
//    protected RemoteCacheManager remoteCacheManager;
}
