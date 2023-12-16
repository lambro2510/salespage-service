package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Notification;
import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends MongoRepository<Notification, ObjectId> {
  Page<Notification> findByUsername(String username, Pageable pageable);

  Notification findNotificationById(String notificationId);

  Page<Notification> findByUsernameAndNotificationType(String username, NotificationType type, Pageable pageable);
  Page<Notification> findByUsernameAndNotificationStatus(String username, NotificationStatus status, Pageable pageable);

  List<Notification> findByUsernameAndNotificationStatus(String username, NotificationStatus status);
}
