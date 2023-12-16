package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.entities.status.NotificationStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/notification")
public class NotificationController extends BaseController {

  @Autowired
  private NotificationService notificationService;

  @GetMapping("{status}")
  @Operation(summary = "Get User Notifications", description = "Retrieve user notifications")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getNotification(Authentication authentication, @PathVariable(required = false) NotificationStatus status, Pageable pageable) throws Exception {
    try {
      return successApi(null, notificationService.getNotification(getUsername(authentication),status, pageable));
    } catch (Exception ex) {
      return errorApiStatus500("Không lưu được thông tin giao dịch");
    }
  }

  @PutMapping("")
  @Operation(summary = "Get User Notifications", description = "Retrieve user notifications")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getNotification(Authentication authentication ) throws Exception {
    try {
      return successApi(null, notificationService.seenALlNotify(getUsername(authentication)));
    } catch (Exception ex) {
      return errorApiStatus500("Không lưu được thông tin giao dịch");
    }
  }

  @GetMapping("detail")
  @Operation(summary = "Get Notification Detail", description = "Retrieve details of a specific notification")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<?> getNotificationDetail(Authentication authentication, @RequestParam String notificationId) throws Exception {
    try {
      return successApi(null, notificationService.getDetail(getUsername(authentication), notificationId));
    } catch (Exception ex) {
      return errorApiStatus500("Không lưu được thông tin giao dịch");
    }
  }
}
