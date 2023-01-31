package com.salespage.salespageservice.app.consumers;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.producer.TopicConfig;
import com.salespage.salespageservice.domains.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductTransactionConsumer {

  @KafkaListener(topics = TopicConfig.SALE_PAGE_PRODUCT_TRANSACTION)
  public void processReturnReward(String message, Acknowledgment acknowledgment) {

    try {
//      ProductTransaction productTransaction = JsonParser.entity(message, ProductTransaction.class);
      log.info(message);

    } catch (Exception e) {
      log.error("====> processReturnReward: ");
    } finally {
      acknowledgment.acknowledge();
    }

  }
}
