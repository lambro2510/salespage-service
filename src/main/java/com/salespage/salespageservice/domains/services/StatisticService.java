package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.Statistic.TotalProductStatisticResponse;
import com.salespage.salespageservice.domains.Constants;
import com.salespage.salespageservice.domains.entities.ProductStatistic;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.StatisticCheckpoint;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class StatisticService extends BaseService {

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
  @Async("threadPoolTaskExecutor")
  public void updateView(String productId){
    Product product = productStorage.findProductById(productId);
    if(Objects.isNull(product)) throw new ResourceNotFoundException("Product not found");
    ProductStatistic statistic = productStatisticStorage.findByProductIdToday(productId);
    if(statistic == null){
      statistic = new ProductStatistic();
      statistic.setDaily(DateUtils.now().toLocalDate());
      statistic.setProductId(productId);
      statistic.setProductName(product.getProductName());
    }
    statistic.addView();

    productStatisticStorage.save(statistic);

  }

  public List<TotalProductStatisticResponse> getStatistic(Long gte, Long lte) {
    LocalDate startDate = DateUtils.convertLongToLocalDateTime(gte).toLocalDate();
    LocalDate endDate = DateUtils.convertLongToLocalDateTime(lte).toLocalDate();

    List<ProductStatistic> productStatistics = productStatisticStorage.findByDailyBetween(startDate, endDate);
    Map<String, TotalProductStatisticResponse> mapProduct = new HashMap<>();

    for (ProductStatistic productStatistic : productStatistics) {
      mapProduct.computeIfAbsent(productStatistic.getProductId(), k -> {
        TotalProductStatisticResponse statistic = new TotalProductStatisticResponse();
        statistic.setProductId(k);
        statistic.setTotalBuy(0L);
        statistic.setTotalPurchase(0L);
        statistic.setTotalUser(0L);
        statistic.setDailies(new ArrayList<>());
        return statistic;
      });

      TotalProductStatisticResponse statistic = mapProduct.get(productStatistic.getProductId());
      partnerToResponse(statistic, productStatistic);
    }

    return new ArrayList<>(mapProduct.values());
  }

  public TotalProductStatisticResponse getStatisticOfProduct(String productId, Long gte, Long lte) {
    TotalProductStatisticResponse statistic = new TotalProductStatisticResponse();
    LocalDate startDate = DateUtils.convertLongToLocalDateTime(gte).toLocalDate();
    LocalDate endDate = DateUtils.convertLongToLocalDateTime(lte).toLocalDate();
    Product product = productStorage.findProductById(productId);
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Product not found");
    List<ProductStatistic> ProductStatistics = productStatisticStorage.findByProductIdAndDailyBetween(productId, startDate, endDate);
    for (ProductStatistic ProductStatistic : ProductStatistics) {
      partnerToResponse(statistic, ProductStatistic);
    }
    return statistic;
  }

  private void partnerToResponse(TotalProductStatisticResponse statistic, ProductStatistic productStatistic) {
    Integer totalView = Math.toIntExact(productStatistic.getTotalView() == null ? 0 : productStatistic.getTotalView());
    statistic.setTotalBuy(statistic.getTotalBuy() + productStatistic.getTotalBuy());
    statistic.setTotalPurchase(statistic.getTotalPurchase() + productStatistic.getTotalPurchase());
    statistic.setTotalUser(statistic.getTotalUser() + productStatistic.getTotalUser());
    statistic.setTotalView(statistic.getTotalView() + totalView);
    TotalProductStatisticResponse.Daily daily = new TotalProductStatisticResponse.Daily();
    daily.setDaily(productStatistic.getDaily());
    daily.setTotalBuy(productStatistic.getTotalBuy());
    daily.setTotalPurchase(productStatistic.getTotalPurchase());
    daily.setTotalUser(productStatistic.getTotalUser());
    daily.setTotalView(Long.valueOf(totalView));
    statistic.getDailies().add(daily);
  }
}
