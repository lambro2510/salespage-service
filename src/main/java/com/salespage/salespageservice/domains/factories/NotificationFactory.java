package com.salespage.salespageservice.domains.factories;

import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

  @Autowired
  NotificationService notificationService;

  public void createNotify(NotificationType type, String name, String username, Double value, String refId, String imageUrl) {
    String title = "Không có nội dung";
    String content = "Không có nội dung";
    switch (type) {
      case PAYMENT_CART_TRANSACTION:
        title = "Bạn đã thanh toán đơn hàng " + refId;
        content = "Đơn hàng " + refId + " đã được xác nhận thanh toán thành công. Tài khoản của bạn đã bị trừ " +
            value.intValue() + "VND, vui lòng chờ cửa hàng xác nhận giao dịch";
        notificationService.createNotification(username, title, content, type, refId);
        break;
      case ADD_TO_CART:
        title = "Bạn đã thêm một sản phẩm mới vào giỏ hàng";
        content = "Bạn đã thêm " + value.intValue() + " sản phẩm " + name + " vào giỏ hàng";
        notificationService.createNotification(username, title, content, type, refId);
        break;
      case PAYMENT_TRANSACTION_IN_SUCCESS:
        title = "Nạp tiền thành công";
        content = "Giao dịch " + refId + " đã được bạn hoàn tất. Tài khoản của bạn đã được cộng thêm " + value + "VNĐ. Vui lòng kiểm tra lại tài khoản.";
        notificationService.createNotification(username, title, content, type, refId);
        break;
      case EXPIRE_PAYMENT:
        title = "Quá hạn giao dịch";
        content = "Giao dịch " + refId + " đã quá thời gian 5 phút. Vui lòng tạo giao dịch mới để tiếp tục nạp tiền";
        notificationService.createNotification(username, title, content, type, refId);
        break;
      case NEW_PAYMENT:
        title = "Tạo thanh toán mới";
        content = "Bạn đang yêu cầu thanh toán " + value.intValue() + " VNĐ. Vui lòng thanh toán trong vòng 5 phút";
        notificationService.createNotification(username, title, content, type, refId);
        break;
    }
  }
}
