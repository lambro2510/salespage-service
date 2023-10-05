package com.salespage.salespageservice.domains.exceptions;


import com.salespage.salespageservice.domains.exceptions.info.BaseException;
import com.salespage.salespageservice.domains.exceptions.info.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughMoneyException extends BaseException {
  public NotEnoughMoneyException() {
    super(ErrorCode.NOT_ENOUGH_MONEY, "Tài khoản của bạn không đủ tiền");
  }

  public NotEnoughMoneyException(String message) {
    super(ErrorCode.ACCOUNT_NOT_EXISTS, message);
  }
}
