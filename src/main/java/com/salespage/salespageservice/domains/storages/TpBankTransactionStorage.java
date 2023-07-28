package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.TpBankTransaction;
import org.springframework.stereotype.Component;

@Component
public class TpBankTransactionStorage extends BaseStorage {

  public TpBankTransaction findByTransId(String id) {
    return tpBankTransactionRepository.findByTransId(id);
  }

  public void save(TpBankTransaction tpBankTransaction) {
    tpBankTransactionRepository.save(tpBankTransaction);
  }
}
