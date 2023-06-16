package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.notificationResponse.NotificationDetailResponse;
import com.salespage.salespageservice.app.responses.notificationResponse.NotificationResponse;
import com.salespage.salespageservice.domains.entities.Notification;
import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService extends BaseService{

  public void createNotification(String username, String tittle, String content){
    Notification notification = new Notification();
    notification.setUsername(username);
    notification.setTittle(tittle);
    notification.setContent(content);
    notification.setNotificationStatus(NotificationStatus.NOT_SEEN);
    notificationStorage.save(notification);
  }

  public PageResponse<NotificationResponse> getNotification(String username, Pageable pageable) {
    Page<Notification> notifications = notificationStorage.findByUsername(username, pageable);
    List<NotificationResponse> listNotification = notifications.getContent().stream().map(Notification::partnerToNotificationResponse).collect(Collectors.toList());
    Page<NotificationResponse> responses = new PageImpl<>(listNotification, pageable, notifications.getTotalElements());
    return PageResponse.createFrom(responses);
  }

  public NotificationDetailResponse getDetail(String username, String notificationId) {
    Notification notification = notificationStorage.findNotificationById(notificationId);
    if(Objects.isNull(notification)) throw new ResourceNotFoundException("Không tìm thấy thông báo");
    if(!notification.getUsername().equals(username)) throw new AuthorizationException("Bạn không có quyền xem thông tin này");
    notification.setNotificationStatus(NotificationStatus.SEEN);
    notification.setUpdatedAt(System.currentTimeMillis());
    notificationStorage.save(notification);
    return notification.partnerToNotificationDetailResponse();
  }
}
