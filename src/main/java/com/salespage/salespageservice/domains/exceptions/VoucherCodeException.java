package com.salespage.salespageservice.domains.exceptions;

import com.salespage.salespageservice.domains.exceptions.info.BaseException;

public class VoucherCodeException extends BaseException {
    public VoucherCodeException(int code, String message) {
        super(code, message);
    }
}
