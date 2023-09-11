package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.PaymentStatistic;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PaymentStatisticStorage extends BaseStorage {
  public void save(PaymentStatistic paymentStatistic) {
    paymentStatisticRepository.save(paymentStatistic);
  }

  public List<PaymentStatistic> findByDailyBetween(LocalDate startDate, LocalDate endDate) {
    return paymentStatisticRepository.findByDailyBetween(startDate, endDate);
  }

  public PaymentStatistic findByDailyAndProductId(LocalDate daily, String productId) {
    return paymentStatisticRepository.findByDailyAndProductId(daily, productId);
  }

  public List<PaymentStatistic> findByProductIdAndDailyBetween(String productId, LocalDate startDate, LocalDate endDate) {
    return paymentStatisticRepository.findByProductIdAndDailyBetween(productId, startDate, endDate);

  }
}
