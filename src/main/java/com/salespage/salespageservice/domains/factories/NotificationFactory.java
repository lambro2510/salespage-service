package com.salespage.salespageservice.domains.factories;

import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

  @Autowired
  NotificationService notificationService;

  public void createNotify(NotificationType type, String name, String username, Double value, String refId) {
    String title = "Không có nội dung";
    String content = "Không có nội dung";
    switch (type) {
      case PAYMENT_CART_TRANSACTION:
        title = "Bạn đã thanh toán đơn hàng" + refId;
        content = "Đơn hàng " + refId + " đã được xác nhận thanh toán thành công. Tài khoản của bạn đã bị trừ " +
            value + "VND, vui lòng chờ cửa hàng xác nhận giao dịch";
        notificationService.createNotification(username, title, content, type, refId);
        break;
      case ADD_TO_CART:
        title = "Bạn đã thêm " + value + " sản phẩm " + name + " vào giỏ hàng";
        content = "Bạn đã thêm " + value + " sản phẩm " + name + " vào giỏ hàng";
        notificationService.createNotification(username, title, content, type, refId);
        break;
    }
  }
}
