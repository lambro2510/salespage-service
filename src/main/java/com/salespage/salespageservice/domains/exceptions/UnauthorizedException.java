package com.salespage.salespageservice.domains.exceptions;


import com.salespage.salespageservice.domains.exceptions.info.BaseException;
import com.salespage.salespageservice.domains.exceptions.info.ErrorCode;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED, "Account not found");
    }
}
