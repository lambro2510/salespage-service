package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Data
@Document("notification")
public class Notification extends BaseEntity{
  @Id
  private ObjectId id;

  @Field("username")
  private String username;

  @Field("content")
  private String content;

  @Field("notification_status")
  private NotificationStatus notificationStatus;
}
