package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductTransactionDetail;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ProductTransactionDetailStorage extends BaseStorage{
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
    List<String> resultList = results.getMappedResults();

    // Lấy số lượng giá trị duy nhất
    return resultList.size();
  }
  public long countByCreatedAtBetween(Long startDay, Long endDay) {
    return productTransactionDetailRepository.countByCreatedAtBetween(startDay,endDay);
  }
}
