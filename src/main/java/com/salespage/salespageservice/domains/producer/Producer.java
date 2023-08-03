package com.salespage.salespageservice.domains.producer;

import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Producer {

  @Autowired
  protected KafkaTemplate<String, String> kafkaTemplate;

  public void createPaymentTransaction(PaymentTransaction paymentTransaction) {
    try {
      log.debug("====write createProductTransaction log success" + paymentTransaction);
      kafkaTemplate.send(TopicConfig.SALE_PAGE_PAYMENT_TRANSACTION, JsonParser.toJson(paymentTransaction));
    } catch (Exception e) {
      log.error(String.valueOf(e));
    }
  }

}

