package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.Statistic.TotalProductStatisticResponse;
import com.salespage.salespageservice.domains.Constants;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.utils.DateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
@Log4j2
public class ProductStatisticService extends BaseService{
  public void asyncStatisticPreDay() {
    LocalDateTime now = DateUtils.nowAtVn();
    log.info("=====>asyncStatisticPreDay");
    List<ProductDetail> productDetails = productDetailStorage.findAll();
    StatisticCheckpoint statisticCheckpoint = statisticCheckpointStorage.findById(Constants.PAYMENT_STATISTIC_CHECKPOINT);
    if (Objects.isNull(statisticCheckpoint)) {
      statisticCheckpoint = new StatisticCheckpoint();
      statisticCheckpoint.setCheckPoint(now.minusDays(64));
      statisticCheckpoint.setId(Constants.PAYMENT_STATISTIC_CHECKPOINT);
    }
    for (LocalDateTime current = statisticCheckpoint.getCheckPoint(); current.isBefore(now); current = current.plusDays(1)) {
      for (ProductDetail productDetail : productDetails) {
        ProductStatistic paymentStatistic = productStatisticStorage.findByDailyAndProductDetailId(current, productDetail.getId().toHexString());
        if (paymentStatistic == null) {
          paymentStatistic = new ProductStatistic();
          paymentStatistic.setDaily(current);
          paymentStatistic.setProductDetailId(productDetail.getId().toHexString());
          paymentStatistic.setProductId(productDetail.getProductId());
        }else{
          TotalProductStatisticResponse totalPaymentStatisticResponse = lookupAggregation(productDetail.getId().toHexString(), current, current.plusDays(1));
          long totalUser = productTransactionDetailStorage.countDistinctUsernameByProductDetailIdAndCreatedAtBetween(productDetail.getId().toHexString(), DateUtils.convertLocalDateTimeToLong(current), DateUtils.convertLocalDateTimeToLong(current.plusDays(1)));
          long totalProduct = productTransactionDetailStorage.countByCreatedAtBetween(DateUtils.convertLocalDateTimeToLong(current), DateUtils.convertLocalDateTimeToLong(current.plusDays(1)));
          totalPaymentStatisticResponse.setTotalProduct(totalProduct);
          totalPaymentStatisticResponse.setTotalUser(totalUser);
          paymentStatistic.partnerFromStatistic(totalPaymentStatisticResponse);
        }
        productStatisticStorage.save(paymentStatistic);
      }

      statisticCheckpoint.setCheckPoint(current.plusDays(1));
      statisticCheckpointStorage.save(statisticCheckpoint);
    }
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public void asyncStatisticToday() {
    log.info("=====>asyncStatisticToday");
    LocalDateTime startDay = DateUtils.startOfDay().toLocalDate().atStartOfDay();
    LocalDateTime endDay = startDay.plusDays(1);
    List<ProductDetail> productDetails = productDetailStorage.findAll();

    for (ProductDetail productDetail : productDetails) {
      ProductStatistic paymentStatistic = productStatisticStorage.findByDailyAndProductDetailId(startDay, productDetail.getId().toHexString());
      if(productDetail.getId().toHexString().equals("6532bc72fae6d41599929249")){
        log.info(123);
      }
      if (paymentStatistic == null) {
        paymentStatistic = new ProductStatistic();
        paymentStatistic.setDaily(startDay);
        paymentStatistic.setProductDetailId(productDetail.getId().toHexString());
        paymentStatistic.setProductId(productDetail.getProductId());
      }else{
        TotalProductStatisticResponse totalPaymentStatisticResponse = lookupAggregation(productDetail.getId().toHexString(), startDay, endDay);
        long totalUser = productTransactionDetailStorage.countDistinctUsernameByProductDetailIdAndCreatedAtBetween(productDetail.getId().toHexString(), DateUtils.convertLocalDateTimeToLong(startDay), DateUtils.convertLocalDateTimeToLong(endDay));
        long totalProduct = productTransactionDetailStorage.countByCreatedAtBetween(DateUtils.convertLocalDateTimeToLong(startDay), DateUtils.convertLocalDateTimeToLong(endDay));
        totalPaymentStatisticResponse.setTotalProduct(totalProduct);
        totalPaymentStatisticResponse.setTotalUser(totalUser);
        paymentStatistic.partnerFromStatistic(totalPaymentStatisticResponse);
      }
      productStatisticStorage.save(paymentStatistic);
    }

  }

  public TotalProductStatisticResponse lookupAggregation(String productId, LocalDateTime gte, LocalDateTime lte) {
    Criteria criteria = Criteria.where("product_detail_id").is(productId)
        .andOperator(Criteria.where("created_at").gte(DateUtils.convertLocalDateTimeToLong(gte)), Criteria.where("created_at").lte(DateUtils.convertLocalDateTimeToLong(lte)));
    AggregationOperation match = Aggregation.match(criteria);
    GroupOperation groupOperation = Aggregation.group()
        .sum("total_price").as("totalPurchase")
        .sum("quantity").as("totalBuy")
        .sum("ship_cod").as("totalShipCod");

    Aggregation aggregation = newAggregation(match, groupOperation);
    AggregationResults<TotalProductStatisticResponse> result
        = mongoTemplate.aggregate(aggregation, "product_transaction_detail", TotalProductStatisticResponse.class);
    TotalProductStatisticResponse response = new TotalProductStatisticResponse();
    if (result.getUniqueMappedResult() != null) {
      response = result.getUniqueMappedResult();
    }
    return response;
  }


  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public void updateToHotProduct(){
    log.info("=====>updateToHotProduct");
    LocalDate now = DateUtils.now().toLocalDate();
    LocalDate preWeek = now.minusWeeks(1);
    LocalDate nextDay = now.plusDays(1);
    List<ProductStatistic> productStatistics = productStatisticStorage.findByDailyBetweenOrderByTotalViewDesc(preWeek, nextDay);
    Map<String, ProductStatistic> topViewProduct = new HashMap<>();
    for(ProductStatistic productStatistic : productStatistics){
      topViewProduct.putIfAbsent(productStatistic.getProductId(), productStatistic);
    }
    int count = 0;
    List<ProductStatistic> statistics = new ArrayList<>(topViewProduct.values());
    List<Product> products = productStorage.findAll();
    Map<String, Product> productMap = products.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));
    for(ProductStatistic statistic: statistics){
      if(count >= 10){
        break;
      }
      Product product = productMap.get(statistic.getProductId());
      if(product != null){
        product.setIsHot(true);
      }
      count++;
    }
    productStorage.saveAll(new ArrayList<>(productMap.values()));
  }
}
