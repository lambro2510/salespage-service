package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.types.LogType;
import com.salespage.salespageservice.domains.storages.*;
import com.salespage.salespageservice.domains.utils.GoogleDriver;
import com.salespage.salespageservice.domains.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

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
    protected VoucherStoreStorage voucherStoreStorage;

    @Autowired
    protected VoucherCodeStorage voucherCodeStorage;

    @Autowired
    protected SellerStoreStorage sellerStoreStorage;

    @Autowired
    protected SystemLogStorage systemLogStorage;

    @Autowired
    protected JwtUtils jwtUtils;

    @Autowired
    @Lazy
    private SystemLogService systemLogService;

    @Autowired
    protected GoogleDriver googleDriver;

    protected void writeLog(String message, String trace, LogType logType, String username){
        systemLogService.createSystemLog(username,message,trace,logType);
    }
}
