package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductStatistic;
import com.salespage.salespageservice.domains.utils.DateUtils;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

  public List<ProductStatistic> findByProductIdToday(String productId) {
    LocalDate now = DateUtils.now().toLocalDate();
    return productStatisticRepository.findByProductIdAndDaily(productId, now);

  }

  public List<ProductStatistic> findTop12ByOrderByTotalBuyDesc() {
    return productStatisticRepository.findTop12ByOrderByTotalBuyDesc();
  }

  public List<ProductStatistic> findTop12ByProductIdInOrderByTotalBuyDesc(List<String> productIds) {
    return productStatisticRepository.findTop12ByProductIdInOrderByTotalBuyDesc(productIds);

  }

  public List<ProductStatistic> findTopNByOrderByTotalBuyDesc(int size) {
    return productStatisticRepository.findTopNByOrderByTotalBuyDesc(size);

  }

  public List<ProductStatistic> findTop12ByProductIdInOrderByTotalViewDesc(List<String> productIds) {
    return productStatisticRepository.findTop12ByProductIdInOrderByTotalViewDesc(productIds);

  }

  public HashSet<String> findDistinctTop10ProductIdByOrderByTotalViewDesc() {
    return productStatisticRepository.findDistinctTop10ProductIdByOrderByTotalViewDesc().stream().map(ProductStatistic::getProductId).collect(Collectors.toCollection(HashSet::new));

  }

  public List<ProductStatistic> findTop12ByOrderByTotalViewDesc() {
    return productStatisticRepository.findTop12ByOrderByTotalViewDesc();
  }

  public void saveAll(List<ProductStatistic> statistics) {
    productStatisticRepository.saveAll(statistics);
  }

  public List<ProductStatistic> findByProductIdIn(List<String> productIds) {
    return productStatisticRepository.findByProductIdIn(productIds);
  }
}
