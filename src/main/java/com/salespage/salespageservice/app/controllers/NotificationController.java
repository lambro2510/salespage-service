package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.services.NotificationService;
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

    @GetMapping("")
    public ResponseEntity<?> getNotification(Authentication authentication, Pageable pageable) throws Exception {
        try {
            return successApi(null, notificationService.getNotification(getUsername(authentication), pageable));
        } catch (Exception ex) {
            return errorApiStatus500("Không lưu được thông tin giao dịch");
        }
    }

    @GetMapping("detail")
    public ResponseEntity<?> getNotificationDetail(Authentication authentication, @RequestParam String notificationId) throws Exception {
        try {
            return successApi(null, notificationService.getDetail(getUsername(authentication), notificationId));
        } catch (Exception ex) {
            return errorApiStatus500("Không lưu được thông tin giao dịch");
        }
    }
}
