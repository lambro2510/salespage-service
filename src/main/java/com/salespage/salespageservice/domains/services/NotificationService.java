package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.notificationResponse.NotificationDetailResponse;
import com.salespage.salespageservice.app.responses.notificationResponse.NotificationResponse;
import com.salespage.salespageservice.domains.entities.Notification;
import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService extends BaseService {

  public void createNotification(String username, String title, String content, NotificationType notificationType, String refId, String imgUrl) {
    Notification notification = new Notification();
    notification.setUsername(username);
    notification.setTitle(title);
    notification.setContent(content);
    notification.setImgUrl(imgUrl);
    notification.setNotificationType(notificationType);
    notification.setRefId(refId);
    notification.setNotificationStatus(NotificationStatus.NOT_SEEN);
    notificationStorage.save(notification);
  }

  public PageResponse<NotificationResponse> getNotification(String username, NotificationStatus status, Pageable pageable) {
    Page<Notification> notifications = notificationStorage.findByUsernameAndNotificationStatus(username,status, pageable);
    List<NotificationResponse> listNotification = notifications.getContent().stream().map(Notification::partnerToNotificationResponse).collect(Collectors.toList());
    Page<NotificationResponse> responses = new PageImpl<>(listNotification, pageable, notifications.getTotalElements());
    return PageResponse.createFrom(responses);
  }

  public NotificationDetailResponse getDetail(String username, String notificationId) {
    Notification notification = notificationStorage.findNotificationById(notificationId);
    if (Objects.isNull(notification)) throw new ResourceNotFoundException("Không tìm thấy thông báo");
    if (!notification.getUsername().equals(username))
      throw new AuthorizationException("Bạn không có quyền xem thông tin này");
    notification.setNotificationStatus(NotificationStatus.SEEN);
    notification.setUpdatedAt(DateUtils.nowInMillis());
    notificationStorage.save(notification);
    return notification.partnerToNotificationDetailResponse();
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public String seenALlNotify(String username) {
    List<Notification> notifications =  notificationStorage.findByUsernameAndNotificationStatus(username, NotificationStatus.NOT_SEEN);
    notifications.forEach(k -> k.setNotificationStatus(NotificationStatus.SEEN));
    notificationStorage.saveAll(notifications);
    return "Thành công";
  }
}
