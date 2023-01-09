package com.salespage.salespageservice.domains.exceptions;


import com.salespage.salespageservice.domains.exceptions.info.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseException {
    private static final long serialVersionUID = 1L;

    public BadRequestException(String exception) {
        super(exception);
    }
}
