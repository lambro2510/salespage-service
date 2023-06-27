package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.TransactionStatistic;
import org.springframework.stereotype.Component;

@Component
public class TransactionStatisticStorage extends BaseStorage{
  public TransactionStatistic findByDate(String date) {
    return transactionStatisticRepository.findByDate(date);
  }

    public void save(TransactionStatistic transactionStatistic) {
    transactionStatisticRepository.save(transactionStatistic);
    }

  public TransactionStatistic findByDateAndProductId(String date, String productId) {
    return transactionStatisticRepository.findByDateAndProductId(date, productId);
  }
}
