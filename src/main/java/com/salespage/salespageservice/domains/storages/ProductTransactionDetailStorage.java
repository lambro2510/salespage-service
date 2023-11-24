package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductTransactionDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ProductTransactionDetailStorage extends BaseStorage{
  public void saveAll(List<ProductTransactionDetail> transactionDetails) {
    productTransactionDetailRepository.saveAll(transactionDetails);
  }

  public List<ProductTransactionDetail> findByTransactionIdIn(List<String> tranIds) {
    return productTransactionDetailRepository.findByTransactionIdIn(tranIds);
  }

  public long countDistinctUsernameByCreatedAtBetween(Long startDay, Long endDay) {
    return productTransactionDetailRepository.countDistinctUsernameByCreatedAtBetween(startDay,endDay);
  }

  public long countByCreatedAtBetween(Long startDay, Long endDay) {
    return productTransactionDetailRepository.countByCreatedAtBetween(startDay,endDay);
  }
}
