package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.Statistic.TotalProductStatisticResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.entities.ProductStatistic;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class StatisticService extends BaseService {

  @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
  @Async("threadPoolTaskExecutor")
  public void updateView(String productId) {
    ProductStatistic statistic = productStatisticStorage.findFirstByProductIdAndDailyOrderByTotalViewAsc(productId);
    statistic.setTotalView(statistic.getTotalView() + 1);
    productStatisticStorage.save(statistic);
  }

  public List<TotalProductStatisticResponse> getStatistic(Long gte, Long lte) {
    List<TotalProductStatisticResponse> responses = new ArrayList<>();
    List<Product> products = productStorage.findAll();
    for (Product product : products) {
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
    for (ProductDetail productDetail : productDetails) {
      List<ProductStatistic> productStatistics = productStatisticStorage.findByProductDetailIdAndDailyBetween(productDetail.getId().toHexString(), startDate, endDate);
      if (productStatistics.isEmpty()) {
        ProductStatistic productStatistic = new ProductStatistic();
        productStatistic.setProductId(productId);
        productStatistic.setProductDetailId(productDetail.getId().toHexString());
        productStatistic.setDaily(DateUtils.now().toLocalDate());
        productStatisticStorage.save(productStatistic);
        productStatistics.add(productStatistic);
      }
      for (ProductStatistic productStatistic : productStatistics) {
        partnerToResponse(statistic, productStatistic, productDetail, product);
      }
    }
    return statistic;
  }

  private void partnerToResponse(TotalProductStatisticResponse statistic, ProductStatistic productStatistic, ProductDetail productDetail, Product product) {
    Integer totalView = Math.toIntExact(productStatistic.getTotalView() == null ? 0 : productStatistic.getTotalView());
    statistic.setProductId(product.getId().toHexString());
    statistic.setProductName(product.getProductName());
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

    TotalProductStatisticResponse.ProductDetailStatistic productDetailStatistic = new TotalProductStatisticResponse.ProductDetailStatistic();
    productDetailStatistic.setProductDetailId(productDetail.getId().toHexString());
    productDetailStatistic.setDaily(productStatistic.getDaily());
    productDetailStatistic.setTotalBuy(productDetailStatistic.getTotalBuy() + productStatistic.getTotalBuy());
    productDetailStatistic.setTotalPurchase(productDetailStatistic.getTotalPurchase() + productStatistic.getTotalPurchase());
    productDetailStatistic.setTotalUser(productDetailStatistic.getTotalUser() + productStatistic.getTotalUser());
    productDetailStatistic.setTotalView(productDetailStatistic.getTotalView() + productStatistic.getTotalView());
    productDetailStatistic.getDailies().add(daily);

    statistic.getProductDetails().add(productDetailStatistic);
  }
}
