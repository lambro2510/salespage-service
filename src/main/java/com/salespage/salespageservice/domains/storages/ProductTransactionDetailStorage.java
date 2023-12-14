package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductTransactionDetail;
import com.salespage.salespageservice.domains.info.AggregationInfo;
import com.salespage.salespageservice.domains.utils.CacheKey;
import com.salespage.salespageservice.domains.utils.JsonParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ProductTransactionDetailStorage extends BaseStorage {
  public void saveAll(List<ProductTransactionDetail> transactionDetails) {
    productTransactionDetailRepository.saveAll(transactionDetails);
  }

  public List<ProductTransactionDetail> findByTransactionIdIn(List<String> tranIds) {
    return productTransactionDetailRepository.findByTransactionIdIn(tranIds);
  }

  public long countDistinctUsernameByProductDetailIdAndCreatedAtBetween(String productDetailId, Long start, Long end) {
    Criteria criteria = Criteria.where("product_detail_id").is(productDetailId)
        .andOperator(
            Criteria.where("created_at")
                .gte(start),
            Criteria.where("created_at")
                .lte(end))
        .and("username").exists(true);

    MatchOperation matchStage = Aggregation.match(criteria);
    GroupOperation groupStage = Aggregation.group("username");

    Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage);

    AggregationResults<String> results = mongoTemplate.aggregate(aggregation, "product_transaction_detail", String.class);
    Set<String> usernames = new HashSet<>();
    for (String data : results.getMappedResults()) {
      AggregationInfo info = JsonParser.entity(data, AggregationInfo.class);
      if(info != null){
        usernames.add(info.getKey());
      }
    }
    // Lấy số lượng giá trị duy nhất
    return usernames.size();
  }

  public long countDistinctUsernameByProductIdAndCreatedAtBetween(String productId, Long start, Long end) {
    Criteria criteria = Criteria.where("product_detail.product_id").is(productId)
        .andOperator(
            Criteria.where("created_at")
                .gte(start),
            Criteria.where("created_at")
                .lte(end))
        .and("username").exists(true);

    MatchOperation matchStage = Aggregation.match(criteria);
    GroupOperation groupStage = Aggregation.group("username");

    Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage);

    AggregationResults<String> results = mongoTemplate.aggregate(aggregation, "product_transaction_detail", String.class);
    Set<String> usernames = new HashSet<>();
    for (String data : results.getMappedResults()) {
      AggregationInfo info = JsonParser.entity(data, AggregationInfo.class);
      if(info != null){
        usernames.add(info.getKey());
      }
    }
    // Lấy số lượng giá trị duy nhất
    return usernames.size();
  }

  public long countByProductDetailIdAndCreatedAtBetween(String id, Long startDay, Long endDay) {
    return productTransactionDetailRepository.countByProductDetailIdAndCreatedAtBetween(id, startDay, endDay);
  }

  public Page<ProductTransactionDetail> findAll(Query query, Pageable pageable) {
    return productTransactionDetailRepository.findAll(query, pageable);
  }

  public List<ProductTransactionDetail> findAll() {
    return productTransactionDetailRepository.findAll();
  }
}
