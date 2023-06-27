package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends MongoRepository<Notification, ObjectId> {
    Page<Notification> findByUsername(String username, Pageable pageable);

    Notification findNotificationById(String notificationId);
}
