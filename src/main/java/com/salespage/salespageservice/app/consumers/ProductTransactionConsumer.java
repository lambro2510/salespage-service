package com.salespage.salespageservice.app.consumers;

import com.salespage.avro.CreateProductTransactionAvro;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.producer.TopicConfig;
import com.salespage.salespageservice.domains.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductTransactionConsumer {

  @Autowired
  private Producer producer;

  @KafkaListener(topics = TopicConfig.SALE_PAGE_PRODUCT_TRANSACTION)
  public void processReturnReward(CreateProductTransactionAvro message) {
    log.debug("====> processReturnReward: {} " + message);
    ProductTransaction productTransaction = new ProductTransaction();
    try {
//      productTransaction = JsonParser.entity(message, ProductTransaction.class);
      log.info(message.getProductId());
      if (productTransaction != null) {
        log.error("===== >");
        throw new Exception();
      }
    } catch (Exception e) {
//      producer.createProductTransaction(productTransaction);
      log.error("====> processReturnReward error: {} " + productTransaction);
    } finally {

    }
  }
}
