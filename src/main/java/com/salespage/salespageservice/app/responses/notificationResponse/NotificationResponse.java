package com.salespage.salespageservice.app.responses.notificationResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationResponse {

  @Schema(description = "ID thông báo")
  private String id;

  @Schema(description = "Tiêu đề thông báo")
  private String title;

  @Schema(description = "Ngày tạo thông báo", example = "2023-08-07 10:30:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date created;

  @Schema(description = "Trạng thái thông báo")
  private NotificationStatus status;
}
