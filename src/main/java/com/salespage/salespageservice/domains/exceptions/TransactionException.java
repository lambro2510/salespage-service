package com.salespage.salespageservice.domains.exceptions;

import com.salespage.salespageservice.domains.exceptions.info.BaseException;
import com.salespage.salespageservice.domains.exceptions.info.ErrorCode;

public class TransactionException extends BaseException {
    public TransactionException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

    public TransactionException(int code, String message) {
        super(code, message);
    }
}
