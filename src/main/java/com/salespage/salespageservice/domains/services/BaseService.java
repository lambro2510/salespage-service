package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.storages.AccountStorage;
import com.salespage.salespageservice.domains.storages.ProductStorage;
import com.salespage.salespageservice.domains.storages.ProductTransactionStorage;
import com.salespage.salespageservice.domains.storages.UserStorage;
import com.salespage.salespageservice.domains.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {
    @Autowired
    protected AccountStorage accountStorage;

    @Autowired
    protected UserStorage userStorage;

    @Autowired
    protected ProductStorage productStorage;

    @Autowired
    protected ProductTransactionStorage productTransactionStorage;

    @Autowired
    protected JwtUtils jwtUtils;

}
