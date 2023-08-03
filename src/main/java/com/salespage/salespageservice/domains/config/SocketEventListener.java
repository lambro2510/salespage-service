package com.salespage.salespageservice.domains.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
@Log4j2
public class SocketEventListener implements WebSocketHandler {
  @Override
  public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

  }

  @Override
  public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

  }

  @Override
  public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

  }

  @Override
  public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }
}
