package com.salespage.salespageservice.domains.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TopicConfig {
  public static final String SALE_PAGE_PAYMENT_TRANSACTION = "salepage.sale.payment.transaction";
  public static final String LIKE_TOPIC = "salepage.sale.like.topic";
    public static final String CHECK_IN_TOPIC = "salepage.sale.check-in";


  @Value("${spring.kafka.topic.replication-factor}")
  private short replicationFactor;

  @Value("${spring.kafka.topic.num-partitions}")
  private int numPartitions;

  private static Map<String, String> defaultConfigs = new HashMap<>();

  static {
    defaultConfigs.put("retention.ms", "604800000"); // 7 day
  }

  @Bean
  public NewTopic createSalePagePayment() {
    NewTopic topic = new NewTopic(SALE_PAGE_PAYMENT_TRANSACTION, numPartitions, replicationFactor);
    topic.configs(defaultConfigs);
    return topic;
  }
  @Bean
  public NewTopic createLikeTopic() {
    NewTopic topic = new NewTopic(LIKE_TOPIC, numPartitions, replicationFactor);
    topic.configs(defaultConfigs);
    return topic;
  }
  @Bean
  public NewTopic createCheckIn() {
    NewTopic topic = new NewTopic(CHECK_IN_TOPIC, numPartitions, replicationFactor);
    topic.configs(defaultConfigs);
    return topic;
  }

}
