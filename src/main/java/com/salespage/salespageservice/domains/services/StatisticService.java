package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.Statistic.TotalProductStatisticResponse;
import com.salespage.salespageservice.domains.Constants;
import com.salespage.salespageservice.domains.entities.ProductDetail;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class StatisticService extends BaseService {

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
  @Async("threadPoolTaskExecutor")
  public void updateView(String productId){
    Product product = productStorage.findProductById(productId);
    if(Objects.isNull(product)) throw new ResourceNotFoundException("Product not found");
    List<ProductStatistic> saveStatistic = new ArrayList<>();
    List<ProductStatistic> statistics = productStatisticStorage.findByProductIdToday(productId);
    Map<String, ProductStatistic> productStatisticMap = statistics.stream().collect(Collectors.toMap(ProductStatistic::getProductDetailId, Function.identity()));
    List<ProductDetail> productDetails = productDetailStorage.findByProductId(productId);

    for (ProductDetail productDetail : productDetails){
      ProductStatistic statistic = productStatisticMap.get(productDetail.getId().toHexString());
      if(statistic == null){
        LocalDate now = DateUtils.now().toLocalDate();
        statistic = new ProductStatistic();
        statistic.setProductDetailId(productDetail.getId().toHexString());
        statistic.setProductId(productDetail.getProductId());
        statistic.setDaily(now);
      }
      statistic.setTotalView(statistic.getTotalView() + 1);
      saveStatistic.add(statistic);
    }

    productStatisticStorage.saveAll(saveStatistic);

  }

  public List<TotalProductStatisticResponse> getStatistic(Long gte, Long lte) {
    List<TotalProductStatisticResponse> responses = new ArrayList<>();
    List<Product> products = productStorage.findAll();
    for(Product product : products){
      TotalProductStatisticResponse response = getStatisticOfProduct(product.getId().toHexString(), gte, lte);
      responses.add(response);
    }

    return responses;
  }

  public TotalProductStatisticResponse getStatisticOfProduct(String productId, Long gte, Long lte) {
    TotalProductStatisticResponse statistic = new TotalProductStatisticResponse();
    LocalDate startDate = DateUtils.convertLongToLocalDateTime(gte).toLocalDate();
    LocalDate endDate = DateUtils.convertLongToLocalDateTime(lte).toLocalDate();
    Product product = productStorage.findProductById(productId);
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Product not found");
    List<ProductDetail> productDetails = productDetailStorage.findByProductId(productId);
    for(ProductDetail productDetail : productDetails){
      List<ProductStatistic> productStatistics = productStatisticStorage.findByProductIdAndDailyBetween(productId, startDate, endDate);
      for (ProductStatistic productStatistic : productStatistics) {
        partnerToResponse(statistic, productStatistic, productDetail);
      }
    }
    return statistic;
  }

  private void partnerToResponse(TotalProductStatisticResponse statistic, ProductStatistic productStatistic, ProductDetail productDetail) {
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

    TotalProductStatisticResponse.ProductDetailStatistic productDetailStatistic= new TotalProductStatisticResponse.ProductDetailStatistic();
    productDetailStatistic.setProductDetailId(productDetail.getId().toHexString());
    productDetailStatistic.setTotalBuy(productDetailStatistic.getTotalBuy() + productStatistic.getTotalBuy());
    productDetailStatistic.setTotalPurchase(productDetailStatistic.getTotalPurchase() + productStatistic.getTotalPurchase());
    productDetailStatistic.setTotalUser(productDetailStatistic.getTotalUser() + productStatistic.getTotalUser());
    productDetailStatistic.setTotalView(productDetailStatistic.getTotalView() + productStatistic.getTotalView());
    productDetailStatistic.getDailies().add(daily);

    statistic.getProductDetails().add(productDetailStatistic);
  }
}
