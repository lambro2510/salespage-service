package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.Statistic.ChartDataResponse;
import com.salespage.salespageservice.app.responses.Statistic.DailyDataResponse;
import com.salespage.salespageservice.app.responses.Statistic.TotalProductStatisticResponse;
import com.salespage.salespageservice.domains.Constants;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatisticService extends BaseService {

  @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
  @Async("threadPoolTaskExecutor")
  public void updateView(String productId) {
    ProductStatistic statistic = productStatisticStorage.findFirstByProductIdAndDailyOrderByTotalViewAsc(productId);
    statistic.setTotalView(statistic.getTotalView() + 1);
    productStatisticStorage.save(statistic);
  }

  public List<ChartDataResponse> getStatistic(Long gte, Long lte) {
    List<TotalProductStatisticResponse> responses = new ArrayList<>();
    List<Product> products = productStorage.findAll();
    LocalDateTime startDate = DateUtils.convertLongToLocalDateTime(gte).toLocalDate().atStartOfDay();
    LocalDateTime endDate = DateUtils.convertLongToLocalDateTime(lte).toLocalDate().plusDays(1).atStartOfDay();
//    LocalDateTime startDateAtVn = DateUtils.convertUtcToVietnamTime(startDate);
//    LocalDateTime endDateAtVn = DateUtils.convertUtcToVietnamTime(endDate);
    for (Product product : products) {
      TotalProductStatisticResponse response = getStatisticOfProduct(product.getId().toHexString(), startDate, endDate);
      responses.add(response);
    }


    List<ChartDataResponse> charts = new ArrayList<>();
    for(TotalProductStatisticResponse response : responses){
      ChartDataResponse chartDataResponse = new ChartDataResponse();
      chartDataResponse.setProductId(response.getProductId());
      chartDataResponse.setProductName(response.getProductName());
      chartDataResponse.setTotalShipCod(response.getTotalShipCod());
      chartDataResponse.setTotalView(response.getTotalView());
      chartDataResponse.setTotalBuy(response.getTotalBuy());
      chartDataResponse.setTotalPurchase(response.getTotalPurchase());
      chartDataResponse.setTotalUser(response.getTotalUser());
      List<String> labels = new ArrayList<>();
      Map<String, List<DailyDataResponse>> dataSetMap = new HashMap<>();
      for(LocalDateTime current = startDate; current.isBefore(endDate.plusDays(1)); current = current.plusDays(1)){
        labels.add(current.toString());
        for(TotalProductStatisticResponse.ProductDetailStatistic productDetailStatistic : response.getProductDetails()){
          for(TotalProductStatisticResponse.Daily daily : productDetailStatistic.getDailies()){
            if(daily.getDaily().equals(current)){
              List<DailyDataResponse> dailies = dataSetMap.get(productDetailStatistic.getProductDetailName());
              if(dailies == null){
                dailies = new ArrayList<>();
              }
              dailies.add(new DailyDataResponse(daily));
              dataSetMap.put(productDetailStatistic.getProductDetailName(), dailies);
            }
          }
        }
      }



      int i = 0;
      List<ChartDataResponse.DataSets> dataSets = new ArrayList<>();
      for(Map.Entry<String, List<DailyDataResponse>> entry : dataSetMap.entrySet()){
        ChartDataResponse.DataSets data = new ChartDataResponse.DataSets();
        data.setLabel(entry.getKey());
        data.setData(entry.getValue());
        data.setBorderColor(Constants.COLOR.get(i++));
        dataSets.add(data);
      }
      chartDataResponse.setLabels(labels);
      chartDataResponse.setDatasets(dataSets);
      charts.add(chartDataResponse);

    }
    return charts;
  }

  public TotalProductStatisticResponse getStatisticOfProduct(String productId, LocalDateTime startDate, LocalDateTime endDate) {
    TotalProductStatisticResponse statistic = new TotalProductStatisticResponse();

    Product product = productStorage.findProductById(productId);
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Product not found");
    List<ProductDetail> productDetails = productDetailStorage.findByProductId(productId);
    Map<String, ProductDetail> detailMap = productDetails.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));
    List<ProductStatistic> productStatistics = productStatisticStorage.findByProductIdAndDailyBetweenOrderByTotalViewAsc(productId, startDate, endDate);

    Map<String, List<ProductStatistic>> productStatisticByDetailMap = productStatistics.stream().collect(Collectors.groupingBy(ProductStatistic::getProductDetailId));
    for(Map.Entry<String, List<ProductStatistic>> entry : productStatisticByDetailMap.entrySet()){
      TotalProductStatisticResponse.ProductDetailStatistic productDetailStatistic = new TotalProductStatisticResponse.ProductDetailStatistic();
      ProductDetail productDetail = detailMap.get(entry.getKey());
      productDetailStatistic.setProductDetailId(productDetail.getId().toHexString());
      productDetailStatistic.setProductDetailName(productDetail.getType().getType());

      for(ProductStatistic productStatistic : entry.getValue()){
        statistic.setProductId(product.getId().toHexString());
        statistic.setProductName(product.getProductName());
        statistic.setTotalBuy(statistic.getTotalBuy() + productStatistic.getTotalBuy());
        statistic.setTotalPurchase(statistic.getTotalPurchase() + productStatistic.getTotalPurchase());
        statistic.setTotalUser(statistic.getTotalUser() + productStatistic.getTotalUser());
        statistic.setTotalView(statistic.getTotalView() + productStatistic.getTotalView());

        productDetailStatistic.setTotalBuy(productDetailStatistic.getTotalBuy() + productStatistic.getTotalBuy());
        productDetailStatistic.setTotalPurchase(productDetailStatistic.getTotalPurchase() + productStatistic.getTotalPurchase());
        productDetailStatistic.setTotalUser(productDetailStatistic.getTotalUser() + productStatistic.getTotalUser());
        productDetailStatistic.setTotalView(productDetailStatistic.getTotalView() + productStatistic.getTotalView());

        TotalProductStatisticResponse.Daily daily = new TotalProductStatisticResponse.Daily();
        daily.setDaily(productStatistic.getDaily());
        daily.setTotalBuy(productStatistic.getTotalBuy());
        daily.setTotalPurchase(productStatistic.getTotalPurchase());
        daily.setTotalUser(productStatistic.getTotalUser());
        daily.setTotalView(productStatistic.getTotalView());
        productDetailStatistic.getDailies().add(daily);
      }
      statistic.getProductDetails().add(productDetailStatistic);
//    }

//    for(ProductStatistic productStatistic : productStatistics){
//      partnerToResponse(statistic, productStatistic, detailMap.get(productStatistic.getProductDetailId()), product);
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
    boolean isContain = false;
    if (productDetail != null) {
      for(TotalProductStatisticResponse.ProductDetailStatistic detailStatistic : statistic.getProductDetails()){
        if(Objects.equals(detailStatistic.getProductDetailId(), productDetail.getId().toHexString())
        ){
          isContain = true;
        }
      }

      if(!isContain){
        productDetailStatistic.setProductDetailId(productDetail.getId().toHexString());
        productDetailStatistic.setProductDetailName(productDetail.getType().getType());
        productDetailStatistic.setTotalBuy(productDetailStatistic.getTotalBuy() + productStatistic.getTotalBuy());
        productDetailStatistic.setTotalPurchase(productDetailStatistic.getTotalPurchase() + productStatistic.getTotalPurchase());
        productDetailStatistic.setTotalUser(productDetailStatistic.getTotalUser() + productStatistic.getTotalUser());
        productDetailStatistic.setTotalView(productDetailStatistic.getTotalView() + productStatistic.getTotalView());
        productDetailStatistic.getDailies().add(daily);
        statistic.getProductDetails().add(productDetailStatistic);
      }else{
        productDetailStatistic.getDailies().add(daily);
      }
    }
  }
}
