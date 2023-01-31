package com.salespage.salespageservice.domains.producer;


import com.salespage.avro.CreateProductTransactionAvro;
import com.salespage.salespageservice.domains.utils.Helper;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Producer {

  @Autowired
  protected KafkaTemplate<String, String> kafkaTemplate;

//  public void createProductTransaction(CreateProductTransactionAvro productTransactionAvro) {
//    try {
//      log.debug("====write placeLogSingle log success" + productTransactionAvro);
//      kafkaTemplate.send(TopicConfig.SALE_PAGE_PRODUCT_TRANSACTION, productTransactionAvro);
//    } catch (Exception e) {
//      e.printStackTrace();
//      log.error(String.valueOf(e));
//    }
//  }

}

