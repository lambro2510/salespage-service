package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.Statistic.TotalPaymentStatistic;
import com.salespage.salespageservice.app.responses.transactionResponse.TotalStatisticResponse;
import com.salespage.salespageservice.domains.Constants;
import com.salespage.salespageservice.domains.entities.PaymentStatistic;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.StatisticCheckpoint;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class PaymentStatisticService extends BaseService{
  public void asyncStatisticPreDay(){
    List<Product> products = productStorage.findAll();
    StatisticCheckpoint statisticCheckpoint = statisticCheckpointStorage.findById(Constants.PAYMENT_STATISTIC_CHECKPOINT);
    if(Objects.isNull(statisticCheckpoint)){
      statisticCheckpoint = new StatisticCheckpoint();
      statisticCheckpoint.setCheckPoint(DateUtils.now().toLocalDate().minusDays(64));
      statisticCheckpoint.setId(Constants.PAYMENT_STATISTIC_CHECKPOINT);
    }
    for(LocalDate current = statisticCheckpoint.getCheckPoint(); current.isBefore(DateUtils.now().toLocalDate()); current = current.plusDays(1)){
      for(Product product : products){
        PaymentStatistic paymentStatistic = lookupAggregation(product.getId().toHexString(), current, current.plusDays(1));
        paymentStatistic.setDaily(current);
        paymentStatistic.setProductId(product.getId().toHexString());
        paymentStatisticStorage.save(paymentStatistic);
        statisticCheckpoint.setCheckPoint(current);
        statisticCheckpointStorage.save(statisticCheckpoint);
      }
    }
  }

  public void asyncStatisticToday(){
    LocalDate startDay = DateUtils.startOfDay().toLocalDate();
    LocalDate endDay = startDay.plusDays(1);
    List<Product> products = productStorage.findAll();

    for(Product product : products){
      PaymentStatistic paymentStatistic = lookupAggregation(product.getId().toHexString(), startDay, endDay);
      paymentStatistic.setDaily(startDay);
      paymentStatistic.setProductId(product.getId().toHexString());
      paymentStatisticStorage.save(paymentStatistic);
    }

  }

  public PaymentStatistic lookupAggregation(String productId, LocalDate gte, LocalDate lte) {
    Criteria criteria = Criteria.where("product_id").is(productId)
        .andOperator(Criteria.where("created_at").gte(DateUtils.convertLocalDateToLong(gte)), Criteria.where("created_at").lte(DateUtils.convertLocalDateToLong(lte)));
    AggregationOperation match = Aggregation.match(criteria);
    GroupOperation groupOperation = Aggregation.group()
        .sum("total_price").as("totalPurchase")
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

  public List<TotalPaymentStatistic> getStatistic(Long gte, Long lte) {
    LocalDate startDate = DateUtils.convertLongToLocalDateTime(gte).toLocalDate();
    LocalDate endDate = DateUtils.convertLongToLocalDateTime(lte).toLocalDate();

    List<PaymentStatistic> paymentStatistics = paymentStatisticStorage.findByDailyBetween(startDate, endDate);
    Map<String, TotalPaymentStatistic> mapProduct = new HashMap<>();

    for (PaymentStatistic paymentStatistic : paymentStatistics) {
      mapProduct.computeIfAbsent(paymentStatistic.getProductId(), k -> {
        TotalPaymentStatistic statistic = new TotalPaymentStatistic();
        statistic.setProductId(k);
        statistic.setTotalBuy(0L);
        statistic.setTotalPurchase(0L);
        statistic.setTotalUser(0L);
        return statistic;
      });

      TotalPaymentStatistic statistic = mapProduct.get(paymentStatistic.getProductId());
      partnerToResponse(statistic, paymentStatistic);
    }

    return new ArrayList<>(mapProduct.values());
  }

  public TotalPaymentStatistic getStatisticOfProduct(String productId, Long gte, Long lte) {
    TotalPaymentStatistic statistic =new TotalPaymentStatistic();
    LocalDate startDate = DateUtils.convertLongToLocalDateTime(gte).toLocalDate();
    LocalDate endDate = DateUtils.convertLongToLocalDateTime(lte).toLocalDate();
    Product product = productStorage.findProductById(productId);
    if(Objects.isNull(product)) throw new ResourceNotFoundException("Product not found");
    List<PaymentStatistic> paymentStatistics = paymentStatisticStorage.findByProductIdAndDailyBetween(productId, startDate, endDate);
    for (PaymentStatistic paymentStatistic : paymentStatistics) {
      partnerToResponse(statistic, paymentStatistic);
    }
    return statistic;
  }

  private void partnerToResponse(TotalPaymentStatistic statistic, PaymentStatistic paymentStatistic) {
    statistic.setTotalBuy(statistic.getTotalBuy() + paymentStatistic.getTotalBuy());
    statistic.setTotalPurchase(statistic.getTotalPurchase() + paymentStatistic.getTotalPurchase());
    statistic.setTotalUser(statistic.getTotalUser() + paymentStatistic.getTotalUser());
    TotalPaymentStatistic.Daily daily = new TotalPaymentStatistic.Daily();
    daily.setDaily(paymentStatistic.getDaily());
    daily.setTotalBuy(paymentStatistic.getTotalBuy());
    daily.setTotalPurchase(paymentStatistic.getTotalPurchase());
    daily.setTotalUser(paymentStatistic.getTotalUser());
    statistic.getDailies().add(daily);
  }
}
