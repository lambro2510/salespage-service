package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.bankDtos.BankDto;
import com.salespage.salespageservice.app.dtos.bankDtos.TransactionData;
import com.salespage.salespageservice.domains.entities.BankTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankService extends BaseService{
  @Value("${casso.client-id}")
  private String clientId;
  public void receiveBankTransaction(BankDto bankDto) {
    List<BankTransaction> bankTransactions = new ArrayList<>();
    for(TransactionData data : bankDto.getData()){
      BankTransaction bankTransaction = new BankTransaction();
      bankTransaction.partnerFromTransactionData(data);
      bankTransactions.add(bankTransaction);
    }
    if(!bankTransactions.isEmpty()){
      bankTransactionStorage.saveAll(bankTransactions);
    }
  }

  public List<BankTransaction> getAllTransaction() {
    return bankTransactionStorage.findAll();
  }

  public String genTransactionQr() {
    return "";
  }

  public void asyncTransaction(String username) {
  }

  public String getOath2Token() {
    return bankTransactionStorage.getOath2Token(clientId);
  }
}
