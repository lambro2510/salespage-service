package com.salespage.salespageservice.app.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
  int errorCode;

  boolean error;

  String message;

  T data;

}
