package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.Constants;
import com.salespage.salespageservice.domains.entities.PaymentStatistic;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.StatisticCheckpoint;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class PaymentStatisticService extends BaseService{
  public void asyncStatisticToday(){
    List<Product> products = productStorage.findAll();
    StatisticCheckpoint statisticCheckpoint = statisticCheckpointStorage.findById(Constants.PAYMENT_STATISTIC_CHECKPOINT);
    if(Objects.isNull(statisticCheckpoint)){
      statisticCheckpoint = new StatisticCheckpoint();
      statisticCheckpoint.setCheckPoint(DateUtils.now().toLocalDate().minusDays(64));
      statisticCheckpoint.setId(Constants.PAYMENT_STATISTIC_CHECKPOINT);
    }
    for(LocalDate current = statisticCheckpoint.getCheckPoint(); current.isBefore(DateUtils.now().toLocalDate()); current = current.plusDays(1)){
      for(Product product : products){
        PaymentStatistic paymentStatistic = lookupAggregation(product.getId(), current, current.plusDays(1));
        paymentStatistic.setDaily(current);
        paymentStatistic.setProductId(product.getId().toHexString());
        paymentStatisticStorage.save(paymentStatistic);
        statisticCheckpoint.setCheckPoint(current);
        statisticCheckpointStorage.save(statisticCheckpoint);
      }
    }
  }

  public PaymentStatistic lookupAggregation(ObjectId productId, LocalDate gte, LocalDate lte) {
    Criteria criteria = Criteria.where("id").is(productId)
        .andOperator(Criteria.where("created_at").gte(gte), Criteria.where("created_at").lte(lte));
    AggregationOperation match = Aggregation.match(criteria);
    GroupOperation groupOperation = Aggregation.group()
        .sum("total_price").as("totalPrice")
        .sum("ship_cod").as("totalShipCod");

    Aggregation aggregation = newAggregation(match, groupOperation);
    AggregationResults<PaymentStatistic> result
        = mongoTemplate.aggregate(aggregation, "product_transaction", PaymentStatistic.class);
    PaymentStatistic response = new PaymentStatistic();
    if(result.getUniqueMappedResult() != null){
      response = result.getUniqueMappedResult();
    }
    return response;
  }
}
