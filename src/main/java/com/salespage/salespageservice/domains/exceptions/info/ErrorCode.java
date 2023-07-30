package com.salespage.salespageservice.domains.exceptions.info;


public class ErrorCode {
    //Transaction
    public static final int NOT_ENOUGH_MONEY = 1;

    //Voucher
    public static final int LIMIT_RECEIVE_VOUCHER = 20;

    public static final int EXPIRE_VOUCHER = 20;
    public static final int NOT_VALID = 1000;
    public static final int BAD_REQUEST = 1001;
    public static final int UNAUTHORIZED = 1002;
    public static final int RESOURCE_FOUND = 1003;
    public static final int AUTHORIZATION = 1004;
    public static final int RESOURCE_NOT_FOUND = 1005;
    public static final int WRONG_ACCOUNT_OR_PASSWORD = 1006;
    public static final int ACCOUNT_NOT_EXISTS = 1007;
}
