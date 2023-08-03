package com.salespage.salespageservice.domains.entities.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationMessage {
  CHANGE_STATUS_PAYMENT_PENDING("Giao dịch đã bị chuyển về trạng thái chờ", "Giao dịch đã bị chuyển về trạng thái chờ do đã quá hạn xử lý"),
  CHANGE_STATUS_PAYMENT_RESOLVE_IN("Giao dịch thành công", "Hệ thống đã nhận được yêu cầu chuyển tiền. Vui long chuyển tiền đến số tài khoản theo mã QR để hoàn tất quá trình chuyển tiền"),
  CHANGE_STATUS_PAYMENT_RESOLVE_OUT_ERR("Giao dịch không thành công", "Tài khoản của bạn không đủ tiền để rút."),
  CHANGE_STATUS_PAYMENT_RESOLVE_OUT("Giao dịch thành công", "Hệ thống đã nhận được yêu cầu chuyển tiền. Vui lòng chờ để hệ thống xử lý việc chuyển tiền "),
  PAYMENT_IN_SUCCESS("Chuyển tiền thành công", "Tài khoản của bạn đã được cộng thêm "),
  PAYMENT_OUT_SUCCESS("Chuyển tiền thành công", "Tài khoản của bạn đã bị trừ đi ");
  String tittle;

  String message;

}
