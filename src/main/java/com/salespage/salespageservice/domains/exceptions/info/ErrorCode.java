package com.salespage.salespageservice.domains.exceptions.info;


import com.salespage.salespageservice.domains.exceptions.AuthorizationException;

public class ErrorCode {
    public static final int BAD_REQUEST = 1000;
    public static final int NOT_VALID = 1001;

    public static final int ACCOUNT_NOT_EXISTS = 1005;
    public static final int WRONG_ACCOUNT_OR_PASSWORD = 1006;
    public static final int UNAUTHORIZED = 1007;

    public static final int UNAUTHORIZATION = 1008;

    public static final int RESOURCE_NOT_FOUND = 1020;
    public static final int RESOURCE_FOUND = 1021;
    public static final int INVALID_STATE = 1022;

    public static final int EXPIRE_VOUCHER = 1023;

    public static final int TRANSACTION_EXCEPTION = 1024;
}
