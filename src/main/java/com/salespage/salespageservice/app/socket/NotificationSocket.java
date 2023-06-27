package com.salespage.salespageservice.app.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationSocket {

    @MessageMapping("/notification")
    @SendTo("topic/notification")
    public String sendNotification(String message){
        return message + "-----------------send";
    }
}
