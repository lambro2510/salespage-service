package com.salespage.salespageservice.domains.producer;

import com.salespage.salespageservice.domains.entities.ProductTransaction;
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

  public void createProductTransaction(ProductTransaction productTransaction) {
    try {
      log.debug("====write createProductTransaction log success" + productTransaction);
      kafkaTemplate.send(TopicConfig.SALE_PAGE_PRODUCT_TRANSACTION, JsonParser.toJson(productTransaction));
    } catch (Exception e) {
      log.error(String.valueOf(e));
    }
  }

}

