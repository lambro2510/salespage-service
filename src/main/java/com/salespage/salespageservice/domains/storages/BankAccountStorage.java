package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountStorage extends BaseStorage{
  public BankAccount findBankAccountById(String bankAccountId) {
    return bankAccountRepository.findBankAccountById(bankAccountId);
  }

  public void save(BankAccount bankAccount) {
    bankAccountRepository.save(bankAccount);
  }
}
