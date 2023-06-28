package com.salespage.salespageservice.app.socket;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificationSocket {

    @MessageMapping("/ws")
    @SendTo("/topic/app")
    public String send(@Payload String message) {
        return message;
    }
}

