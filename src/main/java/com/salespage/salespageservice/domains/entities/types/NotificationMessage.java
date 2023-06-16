package com.salespage.salespageservice.domains.entities.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationMessage {
  CHANGE_STATUS_PAYMENT_PENDING("Giao dịch đã bị chuyển trạng thái", "Giao dịch đã bị chuyển về trạng thái chờ do không nhận được dữ liệu xử lý"),
  CHANGE_STATUS_PAYMENT_RESOLVE_IN("Giao dịch thành công", "Hệ thống đã nhận được tiền bạn chuyển. Tài khoản của bạn đã được cộng thêm "),
  CHANGE_STATUS_PAYMENT_RESOLVE_OUT_ERR("Giao dịch không thành công", "Tài khoản của bạn không đủ tiền để rút."),
  CHANGE_STATUS_PAYMENT_RESOLVE_OUT("Giao dịch thành công", "Hệ thống đã chuyển tiền vào tài khoản của bạn. Tài khoản của bạn đã bị trừ đi ");
  String tittle;

  String message;

}
