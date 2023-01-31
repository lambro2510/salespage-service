package com.salespage.salespageservice.domains.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TopicConfig {
  public static final String SALE_PAGE_PRODUCT_TRANSACTION = "bizfly-7-413-ProductTransaction";


//  @Value("${spring.kafka.topic.replication-factor}")
//  private short replicationFactor;
//
//  @Value("${spring.kafka.topic.num-partitions}")
//  private int numPartitions;
//
//  private static Map<String, String> defaultConfigs = new HashMap<>();
//
//  static {
//    defaultConfigs.put("retention.ms", "604800000"); // 7 day
//  }
//
//  @Bean
//  public NewTopic createPlaceLogSingle() {
//    NewTopic topic = new NewTopic(SALE_PAGE_PRODUCT_TRANSACTION, numPartitions, replicationFactor);
//    topic.configs(defaultConfigs);
//    return topic;
//  }

}
