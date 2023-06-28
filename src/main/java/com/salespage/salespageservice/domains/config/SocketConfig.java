package com.salespage.salespageservice.domains.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class SocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(notificationWebSocketHandler(), "/ws")
            .setAllowedOrigins("*")
            .withSockJS();
  }

  @Bean
  public WebSocketHandler notificationWebSocketHandler() {
    return new SocketEventListener();
  }
}

