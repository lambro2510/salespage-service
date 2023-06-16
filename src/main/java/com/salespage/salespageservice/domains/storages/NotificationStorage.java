package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public class NotificationStorage extends BaseStorage{
  public Page<Notification> findByUsername(String username, Pageable pageable) {
    return notificationRepository.findByUsername(username, pageable);
  }

  public Notification findNotificationById(String notificationId) {
    return notificationRepository.findNotificationById(notificationId);
  }

  public void save(Notification notification) {
    notificationRepository.save(notification);
  }
}
