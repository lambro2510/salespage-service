package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Notification;
import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class NotificationStorage extends BaseStorage {
  public Page<Notification> findByUsernameAndNotificationType(String username, NotificationType type, Pageable pageable) {
    return notificationRepository.findByUsernameAndNotificationType(username,type, pageable);
  }
  public Page<Notification> findByUsernameAndNotificationStatus(String username, NotificationStatus status, Pageable pageable) {
    return notificationRepository.findByUsernameAndNotificationStatus(username,status, pageable);
  }

  public List<Notification> findByUsernameAndNotificationStatus(String username, NotificationStatus status) {
    return notificationRepository.findByUsernameAndNotificationStatus(username,status);
  }

  public Notification findNotificationById(String notificationId) {
    return notificationRepository.findNotificationById(notificationId);
  }

  public void save(Notification notification) {
    notificationRepository.save(notification);
  }

  public void saveAll(List<Notification> notifications) {
    notificationRepository.saveAll(notifications);
  }
}
