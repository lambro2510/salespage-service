package com.salespage.salespageservice.app.socket;

import lombok.Data;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Principal;

@Controller
public class NotificationSocket {

    @MessageMapping("/secured/room")
    public void sendSpecific(
            @Payload Message msg,
            Principal user,
            @Header("simpSessionId") String sessionId) throws Exception {
        System.out.println(1235);
    }


}

