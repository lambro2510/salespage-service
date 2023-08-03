package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.TransactionStatistic;
import com.salespage.salespageservice.domains.entities.types.StatisticType;
import org.springframework.stereotype.Component;

@Component
public class TransactionStatisticStorage extends BaseStorage {
  public TransactionStatistic findByDate(String date) {
    return transactionStatisticRepository.findByDate(date);
  }

  public void save(TransactionStatistic transactionStatistic) {
    transactionStatisticRepository.save(transactionStatistic);
  }

  public TransactionStatistic findByDateAndProductIdAndStatisticType(String date, String productId, StatisticType statisticType) {
    return transactionStatisticRepository.findByDateAndProductIdAndStatisticType(date, productId, statisticType);
  }
}
