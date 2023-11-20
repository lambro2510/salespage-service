package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.Statistic.TotalProductStatisticResponse;
import com.salespage.salespageservice.domains.Constants;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.entities.ProductStatistic;
import com.salespage.salespageservice.domains.entities.StatisticCheckpoint;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class ProductStatisticService extends BaseService{
  public void asyncStatisticPreDay() {
    List<ProductDetail> productDetails = productDetailStorage.findAll();
    StatisticCheckpoint statisticCheckpoint = statisticCheckpointStorage.findById(Constants.PAYMENT_STATISTIC_CHECKPOINT);
    if (Objects.isNull(statisticCheckpoint)) {
      statisticCheckpoint = new StatisticCheckpoint();
      statisticCheckpoint.setCheckPoint(DateUtils.now().toLocalDate().minusDays(64));
      statisticCheckpoint.setId(Constants.PAYMENT_STATISTIC_CHECKPOINT);
    }
    for (LocalDate current = statisticCheckpoint.getCheckPoint(); current.isBefore(DateUtils.now().toLocalDate()); current = current.plusDays(1)) {
      for (ProductDetail productDetail : productDetails) {
        ProductStatistic paymentStatistic = productStatisticStorage.findByDailyAndProductDetailId(current, productDetail.getId().toHexString());
        if (paymentStatistic == null) {
          paymentStatistic = new ProductStatistic();
          paymentStatistic.setDaily(current);
          paymentStatistic.setProductDetailId(productDetail.getId().toHexString());
          paymentStatistic.setProductId(productDetail.getProductId());
        }else{
          TotalProductStatisticResponse totalPaymentStatisticResponse = lookupAggregation(productDetail.getId().toHexString(), current, current.plusDays(1));
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
    LocalDate startDay = DateUtils.startOfDay().toLocalDate();
    LocalDate endDay = startDay.plusDays(1);
    List<ProductDetail> productDetails = productDetailStorage.findAll();

    for (ProductDetail productDetail : productDetails) {
      ProductStatistic paymentStatistic = productStatisticStorage.findByDailyAndProductDetailId(startDay, productDetail.getId().toHexString());
      if (paymentStatistic == null) {
        paymentStatistic = new ProductStatistic();
        paymentStatistic.setDaily(startDay);
        paymentStatistic.setProductDetailId(productDetail.getId().toHexString());
        paymentStatistic.setProductId(productDetail.getProductId());
      }else{
        if(paymentStatistic.getId().toHexString().equals("655b88b2c1001b2f6b0b67eb")){
          System.out.println(123);
        }
        TotalProductStatisticResponse totalPaymentStatisticResponse = lookupAggregation(productDetail.getId().toHexString(), startDay, endDay);
        paymentStatistic.partnerFromStatistic(totalPaymentStatisticResponse);
      }
      productStatisticStorage.save(paymentStatistic);
    }

  }

  public TotalProductStatisticResponse lookupAggregation(String productId, LocalDate gte, LocalDate lte) {
    Criteria criteria = Criteria.where("product_detail_id").is(productId)
        .andOperator(Criteria.where("created_at").gte(DateUtils.convertLocalDateToLong(gte)), Criteria.where("created_at").lte(DateUtils.convertLocalDateToLong(lte)));
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
    LocalDate now = DateUtils.now().toLocalDate();
    LocalDate preWeek = now.minusWeeks(1);
    LocalDate nextDay = now.plusDays(1);
    List<ProductStatistic> productStatistics = productStatisticStorage.findByDailyBetween(preWeek, nextDay);
    Map<Long, ProductStatistic> topViewProduct = new HashMap<>();
    for(ProductStatistic productStatistic : productStatistics){
      topViewProduct.put(productStatistic.getTotalView(), productStatistic);
    }
    int count = 0;
    List<ProductStatistic> statistics = new ArrayList<>(topViewProduct.values());
    Map<String, Product> productMap = new HashMap<>();
    for(ProductStatistic statistic: statistics){
      if(count >= 10){
        break;
      }
      Product product = productMap.get(statistic.getProductId());
      if(product != null){
        continue;
      }

      product = productStorage.findProductById(statistic.getProductId());
      productMap.put(product.getId().toHexString(), product);
      count++;
    }
    productStorage.saveAll(new ArrayList<>(productMap.values()));
  }
}
