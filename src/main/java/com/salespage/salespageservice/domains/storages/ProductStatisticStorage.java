package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductStatistic;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductStatisticStorage extends BaseStorage {
  public void save(ProductStatistic ProductStatistic) {
    productStatisticRepository.save(ProductStatistic);
  }

  public void update(ProductStatistic ProductStatistic) {
    productStatisticRepository.save(ProductStatistic);
  }
  public List<ProductStatistic> findByDailyBetween(LocalDate startDate, LocalDate endDate) {
    return productStatisticRepository.findByDailyBetween(startDate, endDate);
  }

  public ProductStatistic findByDailyAndProductId(LocalDate daily, String productId) {
    return productStatisticRepository.findByDailyAndProductId(daily, productId);
  }

  public List<ProductStatistic> findByProductIdAndDailyBetween(String productId, LocalDate startDate, LocalDate endDate) {
    return productStatisticRepository.findByProductIdAndDailyBetween(productId, startDate, endDate);

  }

  public ProductStatistic findByProductIdToday(String productId) {
    LocalDate now = DateUtils.now().toLocalDate();
    return productStatisticRepository.findByProductIdAndDaily(productId, now);

  }

  public List<ProductStatistic> findTop10ByOrderByTotalBuyDesc() {
    return productStatisticRepository.findTop10ByOrderByTotalBuyDesc();
  }

  public List<ProductStatistic> findTop10ByProductIdInOrderByTotalBuyDesc(List<String> productIds) {
    return productStatisticRepository.findTop10ByProductIdInOrderByTotalBuyDesc(productIds);

  }

  public List<ProductStatistic> findTopNByOrderByTotalBuyDesc(int size) {
    return productStatisticRepository.findTopNByOrderByTotalBuyDesc(size);

  }

  public List<ProductStatistic> findTop10ByProductIdInOrderByTotalViewDesc(List<String> productIds) {
    return productStatisticRepository.findTop10ByProductIdInOrderByTotalViewDesc(productIds);

  }

  public HashSet<String> findDistinctTop10ProductIdByOrderByTotalViewDesc() {
    return productStatisticRepository.findDistinctTop10ProductIdByOrderByTotalViewDesc().stream().map(ProductStatistic::getProductId).collect(Collectors.toCollection(HashSet::new));

  }

  public List<ProductStatistic> findTop10ByOrderByTotalViewDesc() {
    return productStatisticRepository.findTop10ByOrderByTotalViewDesc();
  }
}
