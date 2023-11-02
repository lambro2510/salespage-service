package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.ProductTransactionDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductTransactionDetailStorage extends BaseStorage{
  public void saveAll(List<ProductTransactionDetail> transactionDetails) {
    productTransactionDetailRepository.saveAll(transactionDetails);
  }

  public List<ProductTransactionDetail> findByTransactionIdIn(List<String> tranIds) {
    return productTransactionDetailRepository.findByTransactionIdIn(tranIds);
  }
}
