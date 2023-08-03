package com.salespage.salespageservice.app.socket;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class NotificationHandle extends TextWebSocketHandler {
  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws InterruptedException, IOException {

    String payload = message.getPayload();
    JSONObject jsonObject = new JSONObject();
    session.sendMessage(new TextMessage("Hi " + "Here" + " how may we help you?"));
  }
}
