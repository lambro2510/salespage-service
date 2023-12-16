package com.salespage.salespageservice.app.responses.notificationResponse;

import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NotificationResponse {

  @Schema(description = "ID thông báo")
  private String id;

  @Schema(description = "Tiêu đề thông báo")
  private String title;

  @Schema(description = "Ngày tạo thông báo")
  private long createdAt;

  @Schema(description = "Ảnh thông báo")
  private String imgUrl;

  @Schema(description = "Trạng thái thông báo")
  private NotificationStatus status;

  @Schema(description = "Loại thông báo")
  private NotificationType type;
}
