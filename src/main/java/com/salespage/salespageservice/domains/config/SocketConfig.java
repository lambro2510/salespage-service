package com.salespage.salespageservice.domains.config;

import com.salespage.salespageservice.app.socket.NotificationHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
public class SocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/secured/user/queue/specific-user");
    config.setApplicationDestinationPrefixes("/spring-security-mvc-socket");
    config.setUserDestinationPrefix("/secured/user");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/secured/room").withSockJS();
  }
}

