package com.salespage.salespageservice.app.responses.notificationResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NotificationDetailResponse extends NotificationResponse {

  @Schema(description = "Nội dung thông báo")
  private String content;
}
