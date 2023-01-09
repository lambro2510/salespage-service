package com.salespage.salespageservice.domains.exceptions.info;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {

  protected int code;

  public BaseException(int code, String message) {
    super(message);
    this.setCode(code);
  }

  public BaseException(String message) {
    super(message);
  }
}
