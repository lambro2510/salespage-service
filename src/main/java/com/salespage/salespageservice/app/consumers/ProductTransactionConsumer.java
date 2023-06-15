package com.salespage.salespageservice.app.consumers;

import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.producer.TopicConfig;
import com.salespage.salespageservice.domains.services.BankService;
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

  @KafkaListener(topics = TopicConfig.SALE_PAGE_PAYMENT_TRANSACTION)
  public void processReturnReward(String message) {
    log.debug("====> processReturnReward: {} " + message);
    PaymentTransaction paymentTransaction = new PaymentTransaction();
    try {
      paymentTransaction = JsonParser.entity(message, PaymentTransaction.class);
      if (Objects.nonNull(paymentTransaction)) paymentTransactionStorage.save(paymentTransaction);
    } catch (Exception e) {
      log.error("====> processReturnReward error: {} " + paymentTransaction);
//      producer.createPaymentTransaction(paymentTransaction);
    }
  }

  @KafkaListener(topics = "bizfly-7-453-RewardGift", groupId = "bizfly-7-453-RewardGift")
  public void receiveMessage(String message) {
    System.out.println("Received message from bizfly-7-453-RewardGift topic: " + message);
    // Xử lý tin nhắn nhận được từ Kafka
  }
}
