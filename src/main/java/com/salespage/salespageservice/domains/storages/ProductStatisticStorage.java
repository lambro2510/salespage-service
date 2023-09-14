package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductStatistic;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ProductStatisticStorage extends BaseStorage {
  public void save(ProductStatistic ProductStatistic) {
    ProductStatisticRepository.save(ProductStatistic);
  }

  public void update(ProductStatistic ProductStatistic) {
    ProductStatisticRepository.save(ProductStatistic);
  }
  public List<ProductStatistic> findByDailyBetween(LocalDate startDate, LocalDate endDate) {
    return ProductStatisticRepository.findByDailyBetween(startDate, endDate);
  }

  public ProductStatistic findByDailyAndProductId(LocalDate daily, String productId) {
    return ProductStatisticRepository.findByDailyAndProductId(daily, productId);
  }

  public List<ProductStatistic> findByProductIdAndDailyBetween(String productId, LocalDate startDate, LocalDate endDate) {
    return ProductStatisticRepository.findByProductIdAndDailyBetween(productId, startDate, endDate);

  }

  public ProductStatistic findByProductIdToday(String productId) {
    LocalDate now = DateUtils.now().toLocalDate();
    return ProductStatisticRepository.findByProductIdAndDaily(productId, now);

  }
}
