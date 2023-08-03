package com.salespage.salespageservice.app.socket;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

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

