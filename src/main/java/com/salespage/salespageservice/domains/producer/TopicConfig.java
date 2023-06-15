package com.salespage.salespageservice.domains.producer;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {
  public static final String SALE_PAGE_PAYMENT_TRANSACTION = "bizfly-7-453-PaymentTransaction";


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
