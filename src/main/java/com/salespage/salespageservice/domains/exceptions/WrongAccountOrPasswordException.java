package com.salespage.salespageservice.domains.exceptions;


import com.salespage.salespageservice.domains.exceptions.info.BaseException;
import com.salespage.salespageservice.domains.exceptions.info.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WrongAccountOrPasswordException extends BaseException {
  public WrongAccountOrPasswordException() {
    super(ErrorCode.WRONG_ACCOUNT_OR_PASSWORD, "Wrong username or password");
  }
}
