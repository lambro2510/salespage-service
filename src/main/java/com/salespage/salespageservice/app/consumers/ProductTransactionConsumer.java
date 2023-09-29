package com.salespage.salespageservice.app.consumers;

import com.salespage.salespageservice.app.dtos.accountDtos.CheckInDto;
import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.entities.types.NotificationMessage;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.entities.types.PaymentType;
import com.salespage.salespageservice.domains.info.Rating;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.producer.TopicConfig;
import com.salespage.salespageservice.domains.services.AccountService;
import com.salespage.salespageservice.domains.services.BankService;
import com.salespage.salespageservice.domains.services.NotificationService;
import com.salespage.salespageservice.domains.services.ProductService;
import com.salespage.salespageservice.domains.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
public class ProductTransactionConsumer extends BankService {

  @Autowired
  private Producer producer;

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private ProductService productService;
  
  @Autowired
  private AccountService accountService;

  @KafkaListener(topics = TopicConfig.SALE_PAGE_PAYMENT_TRANSACTION)
  public void createPayment(String message) {
    log.debug("====> createPayment: {} ", message);
    PaymentTransaction paymentTransaction = new PaymentTransaction();
    try {
      paymentTransaction = JsonParser.entity(message, PaymentTransaction.class);
      if (Objects.nonNull(paymentTransaction)) paymentTransactionStorage.save(paymentTransaction);
      if (paymentTransaction.getType().equals(PaymentType.IN)) {
        notificationService.createNotification(paymentTransaction.getUsername(), NotificationMessage.PAYMENT_IN.getTittle(), NotificationMessage.PAYMENT_IN.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
      } else {
        notificationService.createNotification(paymentTransaction.getUsername(), NotificationMessage.PAYMENT_OUT.getTittle(), NotificationMessage.PAYMENT_OUT.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
      }
    } catch (Exception e) {
      log.error("====> createPayment error: {} ", paymentTransaction);
    }
  }

  @KafkaListener(topics = TopicConfig.LIKE_TOPIC)
  public void receiveMessage(String message) {
    log.debug("Received message from " + TopicConfig.LIKE_TOPIC + message);
    try{
      Rating rating = JsonParser.entity(message, Rating.class);
      productService.updateRating(rating.getUsername(), rating.getProductId(), rating.getPoint());
    }catch (Exception ex){
      log.error("====> receiveMessage error: {} ", ex.getMessage());
    }
  }

  @KafkaListener(topics = TopicConfig.CHECK_IN_TOPIC)
  public void checkIn(String message) {
    log.debug("Received message from " + TopicConfig.CHECK_IN_TOPIC + message);
    try{
      CheckInDto dto = JsonParser.entity(message, CheckInDto.class);
      accountService.checkIn(dto);
    }catch (Exception ex){
      log.error("====> receiveMessage error: {} ", ex.getMessage());
    }
  }
}
