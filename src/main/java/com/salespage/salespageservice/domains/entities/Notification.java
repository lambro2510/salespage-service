package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.app.responses.notificationResponse.NotificationDetailResponse;
import com.salespage.salespageservice.app.responses.notificationResponse.NotificationResponse;
import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

@Data
@Document("notification")
public class Notification extends BaseEntity{
  @Id
  private ObjectId id;

  @Field("username")
  private String username;

  @Field("tittle")
  private String tittle;

  @Field("content")
  private String content;

  @Field("notification_status")
  private NotificationStatus notificationStatus;

  @Field("notification_type")
  private NotificationType notificationType;

  @Field("ref_id")
  private String refId;

  public NotificationResponse partnerToNotificationResponse(){
    NotificationResponse response = new NotificationDetailResponse();
    response.setId(id.toHexString());
    response.setTittle(tittle);
    response.setCreated(new Date(createdAt));
    response.setStatus(notificationStatus);
    return response;
  }

  public NotificationDetailResponse partnerToNotificationDetailResponse() {
    NotificationDetailResponse response = new NotificationDetailResponse();
    response.setId(id.toHexString());
    response.setTittle(tittle);
    response.setCreated(new Date(createdAt));
    response.setStatus(notificationStatus);
    response.setContent(content);
    return response;
  }
}
