package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.BankTransaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankTransactionStorage extends BaseStorage{
  public void saveAll(List<BankTransaction> bankTransactions) {
    bankTransactionRepository.saveAll(bankTransactions);
  }

  public List<BankTransaction> findAll() {
    return bankTransactionRepository.findAll();
  }
}
